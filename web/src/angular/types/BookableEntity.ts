import { BookableEntityType, Model } from "."

export default interface Floor extends Model {
    name: string

    parentId: string | null
    floorId: string

    type: BookableEntityType | null
}
