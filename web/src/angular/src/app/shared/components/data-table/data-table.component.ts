/* eslint-disable @typescript-eslint/no-explicit-any */
import { CdkDragDrop, moveItemInArray } from "@angular/cdk/drag-drop"
import { Component, ContentChild, ElementRef, EventEmitter, Input, Output, TemplateRef, ViewChild } from "@angular/core"
import { MatTable } from "@angular/material/table"

@Component({
    selector: "data-table",
    templateUrl: "./data-table.component.html",
    styles: [],
})
export class DataTableComponent {
    @Input() public data: any[] | undefined
    @Input() public columns: { key: string; label: string, getter?: (item: any) => any }[] = []
    @Input() public selectable: "single" | "multiple" | "none" = "none"
    @Input() public selected: any[] = []
    @Input() public reorderable = false

    @Output() public readonly selectedChange = new EventEmitter<any[]>()
    @Output() public readonly newOrder = new EventEmitter<void>()

    @ContentChild(TemplateRef) public templateRef: TemplateRef<any> | undefined

    @ViewChild(MatTable) table!: MatTable<any>
    
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

    public onDrop(event: CdkDragDrop<any>) {
        if(!this.reorderable || this.data === undefined) return
        moveItemInArray(this.data, event.previousIndex, event.currentIndex)
        this.table.renderRows()
        this.newOrder.emit()
    }
}
