import { HttpEventType } from "@angular/common/http"
import { Component, EventEmitter, Input, Output } from "@angular/core"
// eslint-disable-next-line import/named
import { FilePondOptions, FilePondFile, FileStatus, FileOrigin, FilePondInitialFile, FilePond } from "filepond"

import { DefaultService, FileUploadDto } from "src/app/core/openapi"


type UpdateFilesEvent = {
    filepond: unknown
    items: FilePondFile[]
}

@Component({
    selector: "file-upload",
    templateUrl: "./file-upload.component.html",
    styles: [],
})
export class FileUploadComponent {
    @Input() placeholder = "Drop files here..."
    @Input() mimeTypes: string[] = []
    @Input() initialFiles: (string | FileUploadDto)[] = []
    @Input() maxFiles: number | undefined = undefined

    @Output() filesUpdated = new EventEmitter<FileUploadDto[]>()

    constructor(private readonly apiService: DefaultService) {}

    protected get initialFileIds(): FilePondInitialFile[] {
        return this.initialFiles.map((file) => {
            return {
                // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                source: typeof file === "string" ? file : file.fileId!,
                options: {
                    type: "local",
                },
            }
        })
    }

    public get filePondOptions(): FilePondOptions {
        const pondOptions: FilePondOptions = {
            allowMultiple: false,
            labelIdle: this.placeholder,
            acceptedFileTypes: this.mimeTypes,
            allowFileTypeValidation: this.mimeTypes.length > 0,
            chunkUploads: false,
            maxFiles: this.maxFiles,

            server: {
                process: {
                    url: "/api/temporary-file-upload",
                    method: "POST",
                    onload: (response) => {
                        return JSON.parse(response).at(0)?.id
                    },
                },
                restore: (uniqueFileId, load, onError) => {
                    this.apiService.apiTemporaryFileUploadIdGet(uniqueFileId).subscribe({
                        next: (file) => {
                            load(new File([file], "", { type: file.type }))
                            return
                        },
                        error: (error) => {
                            console.error(error)
                            onError(`[${error.status}]: ${error.statusText}`)
                        },
                    })
                },
                revert: (uniqueFileId, load, onError) => {
                    this.apiService.apiTemporaryFileUploadIdDelete(uniqueFileId).subscribe({
                        next: () => {
                            load()
                            return
                        },
                        error: (error) => {
                            console.error(error)
                            onError(`[${error.status}]: ${error.statusText}`)
                        },
                    })
                },

                load: (source, load, onError, progress) => {
                    this.apiService.apiFileIdGet(source, "events", true)
                        .subscribe({
                            next: (event) => {

                                if (event.type === HttpEventType.DownloadProgress) {
                                    progress(true, event.loaded, event.total ?? 0)
                                }

                                if (event.type === HttpEventType.ResponseHeader) {
                                    if (event.ok) {
                                        return
                                    }
                                }

                                if (event.type === HttpEventType.Response && event.ok && event.body !== null) {
                                    load(event.body)
                                }

                                if (event.type === HttpEventType.Response && !event.ok) {
                                    onError(`[${event.status}]: ${event.statusText}`)
                                }
                            },
                            error: (error) => {
                                console.error(error)
                                onError(`[${error.status}]: ${error.statusText}`)
                            },
                        })
                },
            },
        }
        return pondOptions
    }

    pondOnFileUpdate(event: UpdateFilesEvent) {
        this.updateFilePondFiles(event.items)
    }

    updateFilePondFiles(filePondFiles: FilePondFile[]) {
        const files: FileUploadDto[] = filePondFiles
            .filter((file) => {
                return file.status === FileStatus.PROCESSING_COMPLETE || file.status === FileStatus.IDLE
            })
            .map((file) => {
                return { fileId: file.serverId, temporary: file.origin !== FileOrigin.LOCAL }
            })

        this.filesUpdated.emit(files)
    }

    pondOnFileProcessed(event: { pond: FilePond }) {
        this.updateFilePondFiles(event.pond.getFiles())
    }
}
