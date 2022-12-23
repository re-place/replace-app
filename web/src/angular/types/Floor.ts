import { Model } from "."

export default interface Floor extends Model {
    name: string
    siteId: string
    planFileId: string | null
}
