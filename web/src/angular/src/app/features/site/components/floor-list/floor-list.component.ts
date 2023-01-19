import { Component, EventEmitter, Input, Output } from "@angular/core"

import { FloorDto } from "src/app/core/openapi"

@Component({
    selector: "floor-list",
    templateUrl: "./floor-list.component.html",
    styles: [],
})
export class FloorListComponent {
    @Input() floors: FloorDto[] | undefined

    @Output() edit = new EventEmitter<FloorDto>()
    @Output() create = new EventEmitter<void>()

    columns = [
        { key: "id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public onEdit(floor: FloorDto) {
        this.edit.emit(floor)
    }

    public onCreate() {
        this.create.emit()
    }

    public getFloorEditLink(floor: FloorDto) {
        return `/floor/${floor.id}/edit`
    }
}
