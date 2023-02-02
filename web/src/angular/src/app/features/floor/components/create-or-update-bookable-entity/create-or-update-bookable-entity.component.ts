import { Component, EventEmitter, Input, Output } from "@angular/core"

@Component({
    selector: "create-or-update-bookable-entity",
    templateUrl: "./create-or-update-bookable-entity.component.html",
    styles: [],
})
export class CreateOrUpdateBookableEntityComponent {
    @Input() createsNewEntity = false
    @Input() name: string | undefined
    @Input() posX: number | undefined
    @Input() posY: number | undefined

    @Output() nameChange = new EventEmitter<string>()
    @Output() posXChange = new EventEmitter<number>()
    @Output() posYChange = new EventEmitter<number>()

    @Output() submitBookableEntity = new EventEmitter<void>()
    @Output() cancel = new EventEmitter<void>()

    get saveText() {
        return this.createsNewEntity ? "Hinzufügen" : "Speichern"
    }

    public onSubmit(event: SubmitEvent) {
        event.preventDefault()
        this.submitBookableEntity.emit()
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
        if (this.createsNewEntity) {
            return "Neues Buchungselement erstellen"
        } else {
            return "Buchungselement bearbeiten"
        }
    }
}
