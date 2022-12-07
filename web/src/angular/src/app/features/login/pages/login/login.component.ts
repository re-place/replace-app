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

    constructor(private readonly authService: AuthService, private readonly router: Router) {}

    login() {
        this.authService.login(this.user, this.password).catch((reason) => {
            this.loginError = reason.error
        })
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
