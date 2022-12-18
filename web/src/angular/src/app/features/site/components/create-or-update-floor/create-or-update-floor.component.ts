import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"
import { SetOptional } from "type-fest"
import { Floor } from "types"

@Component({
    selector: "create-or-update-floor [floor]",
    templateUrl: "./create-or-update-floor.component.html",
    styles: [],
})
export class CreateOrUpdateFloorComponent implements OnChanges {
    @Input() floor!: SetOptional<Floor, "siteId" | "id">
    floorToEdit: SetOptional<Floor, "siteId" | "id"> = { name: "" }

    @Output() submitFloor = new EventEmitter<SetOptional<Floor, "siteId" | "id">>()

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