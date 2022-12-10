import { Component, Input } from "@angular/core"

@Component({
    selector: "loading-state [loading]]",
    templateUrl: "./loading-state.component.html",
    styles: [],
})
export class LoadingStateComponent {
    @Input() loading = false
}
