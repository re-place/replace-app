import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"

import { FloorDto } from "src/app/core/openapi"

@Component({
    selector: "create-or-update-floor [floor]",
    templateUrl: "./create-or-update-floor.component.html",
    styles: [],
})
export class CreateOrUpdateFloorComponent implements OnChanges {
    @Input() floor!: FloorDto
    floorToEdit: FloorDto = { name: "", planFile: undefined }

    @Output() submitFloor = new EventEmitter<FloorDto>()

    ngOnChanges(changes: SimpleChanges): void {
        if (changes["floor"] === undefined) return

        this.floorToEdit = { ...changes["floor"].currentValue }
    }

    get saveText() {
        return this.floor.id === undefined ? "Hinzufügen" : "Speichern"
    }

    public onSubmit(event: SubmitEvent) {
        event.preventDefault()
        this.submitFloor.emit(this.floorToEdit)
    }
}
