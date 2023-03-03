import { Component, Inject } from "@angular/core"
import { MAT_DIALOG_DATA } from "@angular/material/dialog"

@Component({
    selector: "delete-my-booking-dialog",
    templateUrl: "./delete-my-booking-dialog.component.html",
    styles: [],
})
export class DeleteMyBookingDialogComponent {
    constructor(
        @Inject(MAT_DIALOG_DATA) public data: { id: string, from: Date, to: Date },
    ) {}

    dateTimeFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

}
