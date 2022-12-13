import { Component, ContentChild, Input, TemplateRef } from "@angular/core"

@Component({
    selector: "loading-state [loading]",
    templateUrl: "./loading-state.component.html",
    styles: [],
})
export class LoadingStateComponent {
    @Input() loading = false
    @Input() class: string | undefined

    @ContentChild(TemplateRef) content: TemplateRef<unknown> | undefined

    get classes() {
        return `${this.loading ? "!relative" : ""} ${this.class}`
    }
}
