import { Component, Input, OnInit } from "@angular/core"
import { NavigationEnd, Router } from "@angular/router"

import { AuthService } from "src/app/core/services/auth.service"

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
export class UserLayoutComponent implements OnInit {
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
        {
            title: "Standorte",
            route: "/site",
            icon: "location_city",
        },
    ]

    public isCollapsed = true
    public currentRoute = ""
    public currentUserName = ""

    constructor(private readonly router: Router, private readonly authService: AuthService) {}

    public toggleCollapsed(): void {
        this.isCollapsed = !this.isCollapsed
    }

    public logout(): void {
        this.authService.logout()
    }

    protected setCurrentRoute(url: string): void {
        this.currentRoute = this.menuItems.find((item) => url.startsWith(item.route))?.route ?? ""
    }

    ngOnInit(): void {
        this.setCurrentRoute(this.router.url)

        this.router.events.subscribe((event) => {
            if (!(event instanceof NavigationEnd)) {
                return
            }

            this.setCurrentRoute(event.url)
            this.isCollapsed = true
        })

        this.currentUserName = `${this.authService.currentUser?.firstName} ${this.authService.currentUser?.lastName}`
    }
}
