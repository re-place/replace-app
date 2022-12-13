import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"
import { SetOptional } from "type-fest"
import { BookableEntity } from "types"

@Component({
    selector: "create-or-update-bookable-entity [bookableEntity]",
    templateUrl: "./create-or-update-bookable-entity.component.html",
    styles: [],
})
export class CreateOrUpdateBookableEntityComponent implements OnChanges {
    @Input() bookableEntity!: SetOptional<BookableEntity, "floorId" | "parentId" | "_id">
    bookableEntityToEdit: SetOptional<BookableEntity, "floorId" | "parentId" | "_id"> = { name: "" }

    @Output() submitBookableEntity = new EventEmitter<SetOptional<BookableEntity, "floorId" | "parentId" | "_id">>()

    ngOnChanges(changes: SimpleChanges): void {
        if (changes["bookableEntity"] === undefined) return

        this.bookableEntityToEdit = { ...changes["bookableEntity"].currentValue }
    }

    get saveText() {
        return this.bookableEntity._id === undefined ? "Hinzufügen" : "Speichern"
    }

    public onSubmit(event: SubmitEvent) {
        event.preventDefault()
        this.submitBookableEntity.emit(this.bookableEntityToEdit)
    }
}
