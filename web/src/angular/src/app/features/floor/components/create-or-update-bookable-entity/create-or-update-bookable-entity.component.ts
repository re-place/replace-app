import { Component, EventEmitter, Input, Output } from "@angular/core"
import { MatDialog } from "@angular/material/dialog"

import { DeleteBookableEntityDialogComponent } from "../entity-deletion-modal/delete-bookable-entity-dialog.component"
import { BookableEntityTypeDto , BookableEntityDto } from "src/app/core/openapi"


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
    @Input() parentId: string | undefined
    @Input() availableParents: BookableEntityDto[] = []
    @Input() typeId: string | undefined
    @Input() types: BookableEntityTypeDto[] = []

    @Input() children: BookableEntityDto[] = []

    @Output() typeIdChange = new EventEmitter<string>()
    @Output() nameChange = new EventEmitter<string>()
    @Output() posXChange = new EventEmitter<number>()
    @Output() posYChange = new EventEmitter<number>()
    @Output() parentIdChange = new EventEmitter<string | undefined>()

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

    public get typeInput(): string[] {
        return this.typeId !== undefined ? [this.typeId] : []
    }

    public set typeInput(value: string[]) {
        if (value.length === 0) {
            this.typeIdChange.emit(undefined)
            return
        }

        this.typeIdChange.emit(value.filter((v) => v !== this.typeId).at(0))
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

    public get parentIdInput(): string | null {
        return this.parentId ?? null
    }

    public set parentIdInput(value: string | null) {
        this.parentIdChange.emit(value ?? undefined)
    }

    public get childNames(): string {
        return this.children.map(child => child.name).join(", ")
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
