import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"
import { SetOptional } from "type-fest"

import { BookableEntityDto } from "src/app/core/openapi"

@Component({
    selector: "create-or-update-bookable-entity [bookableEntity]",
    templateUrl: "./create-or-update-bookable-entity.component.html",
    styles: [],
})
export class CreateOrUpdateBookableEntityComponent implements OnChanges {
    @Input() bookableEntity!: SetOptional<BookableEntityDto, "floorId" | "parentId" | "id">
    bookableEntityToEdit: SetOptional<BookableEntityDto, "floorId" | "parentId" | "id"> = { name: "", type: undefined }

    @Output() submitBookableEntity = new EventEmitter<SetOptional<BookableEntityDto, "floorId" | "parentId" | "id">>()

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
