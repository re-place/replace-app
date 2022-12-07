import { Component } from "@angular/core"
import { Router } from "@angular/router"

import { AuthService } from "./core/services/auth.service"

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    styles: [],
})
export class AppComponent {
    title = "app"

    constructor(public authService: AuthService, private readonly router: Router) {}
}
