import { Component, Input } from "@angular/core"
import { NavigationEnd, Router } from "@angular/router"

type MenuItem = {
    title: string
    route: string
    icon: string
}

@Component({
    selector: "user-layout",
    templateUrl: "./user-layout.component.html",
    styles: [],
})
export class UserLayoutComponent {
    @Input() public class = ""

    public menuItems: MenuItem[] = [
        {
            title: "Dashboard",
            route: "/dashboard",
            icon: "dashboard",
        },
        {
            title: "Reservation",
            route: "/reservation",
            icon: "calendar_today",
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
