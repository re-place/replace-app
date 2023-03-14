import { Component, EventEmitter, Input, Output } from "@angular/core"
import { MatDialog } from "@angular/material/dialog"
import { BookableEntityTypeDto } from "src/app/core/openapi"

import { DeleteBookableEntityDialogComponent } from "../entity-deletion-modal/delete-bookable-entity-dialog.component"

@Component({
    selector: "create-or-update-bookable-entity",
    templateUrl: "./create-or-update-bookable-entity.component.html",
    styles: [],
})
export class CreateOrUpdateBookableEntityComponent {
    @Input() id: string | undefined
    @Input() name: string | undefined
    @Input() posX: number | undefined
    @Input() posY: number | undefined
    @Input() typeId: string | undefined
    @Input() types: BookableEntityTypeDto[] = []

    @Output() typeIdChange = new EventEmitter<string>()
    @Output() nameChange = new EventEmitter<string>()
    @Output() posXChange = new EventEmitter<number>()
    @Output() posYChange = new EventEmitter<number>()

    @Output() submitBookableEntity = new EventEmitter<void>()
    @Output() cancel = new EventEmitter<void>()
    @Output() delete = new EventEmitter<string>()

    constructor (public dialog: MatDialog) {}

    get saveText() {
        return this.id === undefined ? "Hinzufügen" : "Speichern"
    }

    public onSubmit(event: SubmitEvent) {
        event.preventDefault()
        this.submitBookableEntity.emit()
    }

    public get typeInput(): string | undefined {
        return this.typeId
    }

    public set typeInput(value: string | undefined) {
        this.typeIdChange.emit(value)
    }

    public get nameInput(): string | undefined {
        return this.name
    }

    public set nameInput(value: string | undefined) {
        this.nameChange.emit(value)
    }

    public get posXInput(): number | undefined {
        return this.posX
    }

    public set posXInput(value: number | undefined) {
        this.posXChange.emit(value)
    }

    public get posYInput(): number | undefined {
        return this.posY
    }

    public set posYInput(value: number | undefined) {
        this.posYChange.emit(value)
    }

    public onCancel() {
        this.cancel.emit()
    }

    get titleText() {
        if (this.id === undefined) {
            return "Neues Buchungselement erstellen"
        } else {
            return "Buchungselement bearbeiten"
        }
    }
    public onDelete() {
        this.dialog.open(DeleteBookableEntityDialogComponent, {
            data: {
                id: this.id,
                name: this.name,
            },
        }).afterClosed().subscribe(result => {
            if (result === true && this.id !== undefined) {
                this.delete.emit(this.id)
            }
        })
    }
}
