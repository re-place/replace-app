import { Component } from "@angular/core"
import { Router } from "@angular/router"

import { AppService } from "./services/app.service"

import type { OnInit } from "@angular/core"

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"],
})
export class AppComponent implements OnInit {
    title = "app"

    constructor(public appService: AppService, private readonly router: Router) {}

    ngOnInit(): void {
        if (!this.appService.isLoggedIn) {
            this.router.navigate(["login"])
        }
    }
}
