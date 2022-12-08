import { Injectable } from "@angular/core"

@Injectable({
    providedIn: "root",
})
export class SessionService {
    get sessionToken(): string | null {
        return localStorage.getItem("SESSION_TOKEN")
    }

    set sessionToken(value: string | null) {
        if (value === null) {
            localStorage.removeItem("SESSION_TOKEN")
        } else {
            localStorage.setItem("SESSION_TOKEN", value)
        }
    }
}
