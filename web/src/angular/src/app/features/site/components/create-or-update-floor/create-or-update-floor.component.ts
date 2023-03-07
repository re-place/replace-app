import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"
import { MatDialog } from "@angular/material/dialog"
import { CreateFloorDto, UpdateFloorDto } from "src/app/core/openapi"
import { DeleteFloorDialogComponent } from "../delete-floor-dialog/delete-floor-dialog.component"

@Component({
    selector: "create-or-update-floor [floor]",
    templateUrl: "./create-or-update-floor.component.html",
    styles: [],
})
export class CreateOrUpdateFloorComponent implements OnChanges {
    @Input() floor!: CreateFloorDto | UpdateFloorDto
    floorToEdit: CreateFloorDto | UpdateFloorDto = { name: "", planFile: undefined }
    @Input() id: string | undefined
    @Input() name: string | undefined

    @Output() submitFloor = new EventEmitter<CreateFloorDto | UpdateFloorDto>()
    @Output() cancel = new EventEmitter<void>()
    @Output() delete = new EventEmitter<string>()    
    
    constructor (public dialog: MatDialog) {}

    ngOnChanges(changes: SimpleChanges): void {
        if (changes["floor"] === undefined) return

        this.floorToEdit = { ...changes["floor"].currentValue }
    }

    get saveText() {
        return (this.floorToEdit as UpdateFloorDto).id === undefined ? "Hinzufügen" : "Speichern"
    }

    public onSubmit(event: SubmitEvent) {
        event.preventDefault()
        this.submitFloor.emit(this.floorToEdit)
    }
}
