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

    constructor(private readonly authService: AuthService, private readonly router: Router) {}

    getLoginError() {
        return this.authService.getLoginError()
    }

    login() {
        this.authService.login(this.user, this.password)
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
