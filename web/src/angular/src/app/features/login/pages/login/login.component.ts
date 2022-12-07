import { Component, OnInit } from "@angular/core"
import { Router } from "@angular/router"

import { AuthService } from "src/app/core/services/auth.service"

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styles: [],
})
export class LoginComponent implements OnInit {
    user = ""
    password = ""
    loginError: string | undefined = undefined
    private readonly intendedUrl: string | null = null

    constructor(private readonly authService: AuthService, private readonly router: Router) {
        const intendedUrl = this.router.getCurrentNavigation()?.extras.state?.["intendedUrl"]

        if (typeof intendedUrl === "string") {
            this.intendedUrl = intendedUrl
        }
    }

    async login() {
        await this.authService.login(this.user, this.password).catch((reason) => {
            this.loginError = reason.error
        })

        if (this.intendedUrl !== null) {
            this.router.navigateByUrl(this.intendedUrl)
            return
        }

        this.router.navigateByUrl("/")
    }

    ngOnInit(): void {
        this.authService.isAuthenticated().then((isAuthenticated) => {
            if (!isAuthenticated) {
                return
            }

            this.router.navigateByUrl("/")
        })
    }
}
