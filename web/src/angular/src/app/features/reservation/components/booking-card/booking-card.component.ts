import { Component, EventEmitter, Input, Output } from "@angular/core"

import { Booking } from "../../pages/reservation-overview/reservation-overview.component"
import { BookableEntityDto } from "src/app/core/openapi"


@Component({
    selector: "booking-card [booking]",
    templateUrl: "./booking-card.component.html",
    styles: [],
})
export class BookingCardComponent {

    @Input() booking!: Booking

    @Output() delete = new EventEmitter<Booking>()

    open = false

    deleteBooking() {
        this.delete.emit(this.booking)
    }

    toNameList(entities: BookableEntityDto[]): string {
        return entities.map(entity => entity.name).join(", ")
    }

    dateTimeFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }
}
