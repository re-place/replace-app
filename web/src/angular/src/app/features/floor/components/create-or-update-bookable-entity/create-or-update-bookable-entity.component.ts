import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"
import { SetOptional } from "type-fest"
import { BookableEntity } from "types"

@Component({
    selector: "create-or-update-bookable-entity [bookableEntity]",
    templateUrl: "./create-or-update-bookable-entity.component.html",
    styles: [],
})
export class CreateOrUpdateBookableEntityComponent implements OnChanges {
    @Input() bookableEntity!: SetOptional<BookableEntity, "floorId" | "parentId" | "id">
    bookableEntityToEdit: SetOptional<BookableEntity, "floorId" | "parentId" | "id"> = { name: "", type: null }

    @Output() submitBookableEntity = new EventEmitter<SetOptional<BookableEntity, "floorId" | "parentId" | "id">>()

    ngOnChanges(changes: SimpleChanges): void {
        if (changes["bookableEntity"] === undefined) return

        this.bookableEntityToEdit = { ...changes["bookableEntity"].currentValue }
    }

    get saveText() {
        return this.bookableEntity.id === undefined ? "Hinzufügen" : "Speichern"
    }

    public onSubmit(event: SubmitEvent) {
        event.preventDefault()
        this.submitBookableEntity.emit(this.bookableEntityToEdit)
    }
}
