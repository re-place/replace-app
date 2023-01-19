import { FileUploadDto } from "../core/openapi"

export function urlForFile(file: FileUploadDto): string {
    if (file.temporary === true) {
        return `/api/temporary-file-upload/${file.fileId}`
    }

    return `/api/file/${file.fileId}`
}
