import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"

import { CreateFloorDto, UpdateFloorDto } from "src/app/core/openapi"

@Component({
    selector: "create-or-update-floor [floor]",
    templateUrl: "./create-or-update-floor.component.html",
    styles: [],
})
export class CreateOrUpdateFloorComponent implements OnChanges {
    @Input() floor!: CreateFloorDto | UpdateFloorDto
    floorToEdit: CreateFloorDto | UpdateFloorDto = { name: "", planFile: undefined }

    @Output() submitFloor = new EventEmitter<CreateFloorDto | UpdateFloorDto>()

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
