import { Component } from "@angular/core"
import { Router } from "@angular/router"

import { AuthService } from "./core/services/auth.service"

import type { OnInit } from "@angular/core"

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"],
})
export class AppComponent implements OnInit {
    title = "app"

    constructor(public authService: AuthService, private readonly router: Router) {}

    ngOnInit(): void {
        if (!this.authService.isLoggedIn) {
            this.router.navigate(["login"])
        }
    }
}
