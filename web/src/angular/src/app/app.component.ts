import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AppService} from "./services/app.service";

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"]
})
export class AppComponent implements OnInit {
    title = "app";

    menuOptions: any[] = [
        { display: 'Buchungen', icon: 'library_books', routerLink: '/bookings' },
        { display: 'Administration', icon: 'settings', routerLink: '/admin' },
    ]

    constructor(public appService: AppService, private router: Router) {}

    ngOnInit(): void {
        if(!this.appService.isLoggedIn) {
            this.router.navigate(['login']);
        }
    }
}
