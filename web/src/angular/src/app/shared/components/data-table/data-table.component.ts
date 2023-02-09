/* eslint-disable @typescript-eslint/no-explicit-any */
import { Component, ContentChild, EventEmitter, Input, Output, TemplateRef } from "@angular/core"

@Component({
    selector: "data-table",
    templateUrl: "./data-table.component.html",
    styles: [],
})
export class DataTableComponent {
    @Input() public data: any[] | undefined
    @Input() public columns: { key: string; label: string }[] = []
    @Input() public selectable: "single" | "multiple" | "none" = "none"
    @Input() public selected: any[] = []

    @Output() public readonly selectedChange = new EventEmitter<any[]>()

    @ContentChild(TemplateRef) public templateRef: TemplateRef<any> | undefined

    get dataToDisplay() {
        return this.data ?? []
    }

    get columnsToDisplay() {
        const columns = this.columns.map((column) => column.key)

        if (this.templateRef !== undefined) {
            columns.push("actions")
        }

        return columns
    }

    public select(row: any) {
        if (this.selectable === "none") {
            return
        }

        if (this.selected.includes(row)) {
            this.selectedChange.emit(this.selected.filter((selected) => selected !== row))
            return
        }

        if (this.selectable === "single") {
            this.selectedChange.emit([row])
        }

        if (this.selectable === "multiple") {
            this.selectedChange.emit([...this.selected, row])
        }
    }

    public isSelected(row: any) {
        if (this.selectable === "none") {
            return false
        }

        return this.selected.includes(row)
    }
}
