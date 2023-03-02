import { Component, EventEmitter, Input, Output } from "@angular/core"
import { DeleteFloorDialogComponent } from "../delete-floor-dialog/delete-floor-dialog.component"
import { FloorDto } from "src/app/core/openapi"
import { MatDialog } from "@angular/material/dialog"

@Component({
    selector: "floor-list",
    templateUrl: "./floor-list.component.html",
    styles: [],
})
export class FloorListComponent {
    @Input() floors: FloorDto[] | undefined
    @Input() id: string | undefined
    @Input() name: string | undefined

    @Output() edit = new EventEmitter<FloorDto>()
    @Output() create = new EventEmitter<void>()
    @Output() cancel = new EventEmitter<void>()
    @Output() delete = new EventEmitter<string>()


    constructor (public dialog: MatDialog) {}

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

    public onDelete(floor: FloorDto) {        
        this.dialog.open(DeleteFloorDialogComponent, {
            data: {
                id: floor.id,
                name: floor.name,
            },
        }).afterClosed().subscribe(result => {
            if (result === true && floor.id !== undefined) {
                this.delete.emit(floor.id)
            }
        })
    }
}
