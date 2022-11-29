import { Component } from "@angular/core"

import { AuthService } from "src/app/core/services/auth.service"

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styles: [],
})
export class LoginComponent {
    user = ""
    pass = ""

    constructor(private readonly authService: AuthService) {}

    login() {
        this.authService.login(this.user, this.pass)
    }
}
