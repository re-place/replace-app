import { Model } from "."

export default interface User extends Model {
    username: string
    password: string
    firstName: string
    lastName: string
}
