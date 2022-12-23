import { Component, ContentChild, EventEmitter, Input, Output } from "@angular/core"
import { v4 } from "uuid"

import { HeaderDirective } from "../../directives/header.directive"

@Component({
    selector: "crud-card",
    templateUrl: "./crud-card.component.html",
    styles: [],
})
export class CrudCardComponent {
    @Input() saveText: string | undefined
    @Input() deletable = false

    @Output() save = new EventEmitter<SubmitEvent>()
    @Output() delete = new EventEmitter()

    @ContentChild(HeaderDirective) header: HeaderDirective | undefined

    public formId = v4()

    public submit(event: SubmitEvent) {
        if (this.saveText === null) {
            return
        }

        event.preventDefault()

        this.save.emit(event)
    }

    public emitDelete() {
        if (!this.deletable) {
            return
        }

        this.delete.emit()
    }
}
