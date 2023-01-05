import { HttpEventType } from "@angular/common/http"
import { Component, EventEmitter, Input, Output } from "@angular/core"
// eslint-disable-next-line import/named
import { FilePondOptions, FilePondFile, FileStatus, FileOrigin, FilePondInitialFile, FilePond } from "filepond"
import { File } from "types"

import { ApiService } from "src/app/core/services/api.service"

export type FileUpload = {
    id: string
    temporary: boolean
}

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
    @Input() initialFiles: (string | File)[] = []
    @Input() maxFiles: number | undefined = undefined

    @Output() filesUpdated = new EventEmitter<FileUpload[]>()

    constructor(private readonly apiService: ApiService) {}

    protected get initialFileIds(): FilePondInitialFile[] {
        return this.initialFiles.map((file) => {
            return {
                source: typeof file === "string" ? file : file.id,
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
                restore: (uniqueFileId, load, error) => {
                    this.apiService.getTemporaryFileUpload(uniqueFileId).subscribe((event) => {
                        if (event.type === HttpEventType.ResponseHeader) {
                            if (event.ok) {
                                return
                            }

                            error(`[${event.status}]: ${event.statusText}`)
                        }

                        if (event.type === HttpEventType.Response && event.ok && event.body !== null) {
                            load(event.body)
                        }
                    })
                },
                revert: (uniqueFileId, load, error) => {
                    this.apiService.deleteTemporaryFileUpload(uniqueFileId).subscribe((event) => {
                        if (event.type === HttpEventType.ResponseHeader) {
                            if (event.ok) {
                                load()
                                return
                            }

                            error(`[${event.status}]: ${event.statusText}`)
                        }
                    })
                },

                load: (source, load, error, progress, abort, headers) => {
                    this.apiService.getFile(source).subscribe((event) => {
                        if (event.type === HttpEventType.DownloadProgress) {
                            progress(true, event.loaded, event.total ?? 0)
                        }

                        if (event.type === HttpEventType.ResponseHeader) {
                            if (event.ok) {
                                return
                            }

                            error(`[${event.status}]: ${event.statusText}`)
                        }

                        if (event.type === HttpEventType.Response && event.ok && event.body !== null) {
                            load(event.body)
                        }

                        if (event.type === HttpEventType.Response && !event.ok) {
                            error(`[${event.status}]: ${event.statusText}`)
                        }
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
        const files: FileUpload[] = filePondFiles
            .filter((file) => {
                return file.status === FileStatus.PROCESSING_COMPLETE || file.status === FileStatus.IDLE
            })
            .map((file) => {
                return { id: file.serverId, temporary: file.origin !== FileOrigin.LOCAL }
            })

        this.filesUpdated.emit(files)
    }

    pondOnFileProcessed(event: { pond: FilePond }) {
        this.updateFilePondFiles(event.pond.getFiles())
    }
}
