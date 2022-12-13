import { Component, EventEmitter, Input, Output } from "@angular/core"
import { BookableEntity } from "types"

@Component({
    selector: "bookable-entity-list",
    templateUrl: "./bookable-entity-list.component.html",
    styles: [],
})
export class BookableEntityListComponent {
    @Input() bookableEntities: BookableEntity[] | undefined

    @Output() edit = new EventEmitter<BookableEntity>()
    @Output() create = new EventEmitter<void>()

    columns = [
        { key: "_id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public onEdit(bookableEntity: BookableEntity) {
        this.edit.emit(bookableEntity)
    }

    public onCreate() {
        this.create.emit()
    }
}
