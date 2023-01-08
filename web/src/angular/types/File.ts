export default interface File {
    id: string
    name: string
    path: string
    mime: string | null
    extension: string
    sizeInBytes: number
}
