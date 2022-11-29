import { Component } from "@angular/core"
import { NavigationEnd , Router } from "@angular/router"

@Component({
    selector: "app-user-layout",
    templateUrl: "./user-layout.component.html",
    styleUrls: ["./user-layout.component.scss"],
})
export class UserLayoutComponent {
    public isCollapsed = true
    public currentRoute = ""

    constructor(private readonly router: Router) {
        this.currentRoute = this.router.url
        this.router.events.subscribe((event) => {
            if (!(event instanceof NavigationEnd)) {
                return
            }

            this.currentRoute = event.url
            this.isCollapsed = true
        })
    }

    public toggleCollapsed(): void {
        this.isCollapsed = !this.isCollapsed
    }
}
