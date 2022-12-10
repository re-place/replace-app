/* eslint-disable @typescript-eslint/no-explicit-any */
import { Component, ContentChild, Input, TemplateRef } from "@angular/core"

@Component({
    selector: "data-table",
    templateUrl: "./data-table.component.html",
    styles: [],
})
export class DataTableComponent {
    @Input() public data: any[] | undefined
    @Input() public columns: { key: string; label: string }[] = []

    @ContentChild(TemplateRef) templateRef: TemplateRef<any> | undefined

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
}
