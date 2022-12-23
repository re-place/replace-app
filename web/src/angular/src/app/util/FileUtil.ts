import { File, TemporaryFileUpload } from "types"

export function urlForFile(file: File | string): string {
    const fileId = typeof file === "string" ? file : file.id

    return `/api/file/${fileId}`
}

export function urlForTemporaryFileUpload(file: TemporaryFileUpload | string): string {
    const fileId = typeof file === "string" ? file : file.id

    return `/api/temporary-file-upload/${fileId}`
}
