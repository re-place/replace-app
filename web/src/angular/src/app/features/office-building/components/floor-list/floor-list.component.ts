import { Component, EventEmitter, Input, Output } from "@angular/core"
import { Floor } from "types"

@Component({
    selector: "floor-list",
    templateUrl: "./floor-list.component.html",
    styles: [],
})
export class FloorListComponent {
    @Input() floors: Floor[] | undefined

    @Output() edit = new EventEmitter<Floor>()
    @Output() create = new EventEmitter<void>()

    columns = [
        { key: "_id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public onEdit(floor: Floor) {
        this.edit.emit(floor)
    }

    public onCreate() {
        this.create.emit()
    }
}
