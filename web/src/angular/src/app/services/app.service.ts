import { Injectable } from "@angular/core"
import { Router } from "@angular/router"

@Injectable({
    providedIn: "root",
})
export class AppService {
    constructor(private readonly router: Router) {}

    isLoggedIn = false

    login(user: string, pass: string) {
        // send login request
        this.isLoggedIn = true
        this.router.navigate(["dashboard"])
    }
}
