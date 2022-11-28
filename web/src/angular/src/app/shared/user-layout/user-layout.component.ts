import { Component } from "@angular/core"
import { NavigationEnd , Router } from "@angular/router"

type MenuItem = {
    title: string;
    route: string;
    icon: string;
};

@Component({
    selector: "app-user-layout",
    templateUrl: "./user-layout.component.html",
    styleUrls: ["./user-layout.component.scss"],
})
export class UserLayoutComponent {

    public menuItems: MenuItem[] = [
        {
            title: "Dashboard",
            route: "/dashboard",
            icon: "dashboard",
        },
        {
            title: "Reservation",
            route: "/reservation",
            icon: "calendar",
        },
    ]

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
