import { Component, ContentChild, EventEmitter, Input, Output } from "@angular/core"

import { ActionsDirective } from "../../directives/actions.directive"
import { IconDirective } from "../../directives/icon.directive"

@Component({
    selector: "empty-state",
    templateUrl: "./empty-state.component.html",
    styles: [],
})
export class EmptyStateComponent {
    @Input() title: string | undefined
    @Input() subtitle: string | undefined
    @Input() dashed = false
    @Input() clickable = false

    @Output() actionClick = new EventEmitter<void>()

    @ContentChild(ActionsDirective) actions: ActionsDirective | undefined
    @ContentChild(IconDirective) icon: IconDirective | undefined

    get classes() {
        const clickableClasses = this.clickable ? "w-full hover:border-gray-400" : ""
        const dashedClasses = this.dashed ? "border-2 border-gray-300 border-dashed rounded-lg" : ""

        return `${clickableClasses} ${dashedClasses}`
    }
}
