import { Component, Input } from "@angular/core"

@Component({
    selector: "user-card",
    templateUrl: "./user-card.component.html",
    styles: [],
})
export class UserCardComponent {
    @Input() public name!: string
}
