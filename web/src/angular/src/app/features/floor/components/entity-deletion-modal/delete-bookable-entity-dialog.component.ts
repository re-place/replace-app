import { Component, Inject, OnInit } from "@angular/core"
import { MAT_DIALOG_DATA } from "@angular/material/dialog"

import { BookingDto, DefaultService } from "src/app/core/openapi"
import { DataLoader } from "src/app/util"
import { toLocaleDateTimeString } from "src/app/util/DateTime"

@Component({
    selector: "delete-bookable-entity-dialog",
    templateUrl: "./delete-bookable-entity-dialog.component.html",
    styles: [
    ],
})
export class DeleteBookableEntityDialogComponent implements OnInit {
    constructor(
        @Inject(MAT_DIALOG_DATA) public data: { id: string, name: string },
        private readonly api: DefaultService,
    ) {}

    public bookings = new DataLoader<BookingDto[]>()

    ngOnInit(): void {
        const now = new Date()
        this.bookings.source(() => this.api.apiBookingByParamsGet(now.toISOString(), undefined, this.data.id)).refresh()
    }

    public dateFormat = toLocaleDateTimeString
}
