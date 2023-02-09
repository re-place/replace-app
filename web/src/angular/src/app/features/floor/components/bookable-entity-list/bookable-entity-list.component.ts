import { Component, EventEmitter, Input, Output } from "@angular/core"

import { BookableEntityDto } from "src/app/core/openapi"

@Component({
    selector: "bookable-entity-list",
    templateUrl: "./bookable-entity-list.component.html",
    styles: [],
})
export class BookableEntityListComponent {
    @Input() bookableEntities: BookableEntityDto[] | undefined
    @Input() selectedBookableEntity: BookableEntityDto | undefined

    @Output() selectedBookableEntityChange = new EventEmitter<BookableEntityDto>()
    @Output() create = new EventEmitter<void>()

    public get selectedEntities() {
        return this.selectedBookableEntity === undefined ? [] : [this.selectedBookableEntity]
    }

    public set selectedEntities(selected: BookableEntityDto[]) {
        this.selectedBookableEntityChange.emit(selected.at(0))
    }

    columns = [
        { key: "name", label: "Name" },
    ]

    public onCreate() {
        this.create.emit()
    }
}
