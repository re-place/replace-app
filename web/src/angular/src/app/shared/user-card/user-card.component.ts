import { Component } from "@angular/core"

@Component({
    selector: "user-card",
    templateUrl: "./user-card.component.html",
    styleUrls: ["./user-card.component.scss"],
})
export class UserCardComponent {
    public name = "Max Mustermann"
}
