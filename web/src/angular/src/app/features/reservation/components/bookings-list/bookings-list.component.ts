import { Component, Input } from "@angular/core"

import { Interval } from "../time-selector/time-selector.component"
import { BookingDto } from "src/app/core/openapi"

@Component({
    selector: "bookings-list",
    templateUrl: "./bookings-list.component.html",
    styles: [
    ],
})
export class BookingsListComponent {
    @Input() bookings: BookingDto[] = []
    @Input() interval: Interval = {
        start: new Date(),
        end: new Date(),
    }
}
