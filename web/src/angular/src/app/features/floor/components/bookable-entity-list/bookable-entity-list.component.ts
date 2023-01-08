import { Component, EventEmitter, Input, Output } from "@angular/core"

import { BookableEntityDto } from "src/app/core/openapi"

@Component({
    selector: "bookable-entity-list",
    templateUrl: "./bookable-entity-list.component.html",
    styles: [],
})
export class BookableEntityListComponent {
    @Input() bookableEntities: BookableEntityDto[] | undefined

    @Output() edit = new EventEmitter<BookableEntityDto>()
    @Output() create = new EventEmitter<void>()

    columns = [
        { key: "id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public onEdit(bookableEntity: BookableEntityDto) {
        this.edit.emit(bookableEntity)
    }

    public onCreate() {
        this.create.emit()
    }
}
