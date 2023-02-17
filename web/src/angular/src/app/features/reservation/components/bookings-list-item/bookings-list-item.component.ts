import { Component, Input } from "@angular/core"

import { Interval } from "../time-selector/time-selector.component"
import { BookingDto } from "src/app/core/openapi"
import generateRandomColor from "src/app/util/Random"

@Component({
    selector: "bookings-list-item",
    templateUrl: "./bookings-list-item.component.html",
    styles: [
    ],
})
export class BookingsListItemComponent {
    @Input() booking!: BookingDto
    @Input() fullInterval!: Interval


    public dateTimeFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

    get bookingStartTime() {
        return new Date(this.booking.start as string)
    }

    get bookingEndTime() {
        return new Date(this.booking.end as string)
    }

    get space() {
        return Math.max(this.fullInterval.end.getTime() - this.fullInterval.start.getTime(), 1)
    }

    get startPosition() {
        if (this.bookingStartTime.getTime() < this.fullInterval.start.getTime()) {
            return 0
        }

        return Math.min(Math.floor((this.bookingStartTime.getTime() - this.fullInterval.start.getTime()) / this.space * 100), 100)
    }

    get endPosition() {
        if (this.bookingEndTime.getTime() > this.fullInterval.end.getTime()) {
            return 100
        }

        return Math.min(Math.floor((this.bookingEndTime.getTime() - this.fullInterval.start.getTime()) / this.space * 100), 100)
    }

    get width() {
        return this.endPosition - this.startPosition
    }

    get bookedEntitiesList() {
        return this.booking.bookedEntities?.map(e => e.name as string) ?? []
    }

    get color() {
        return generateRandomColor(this.booking.userId as string)
    }

    get bottomBorderStyle() {
        return `width: ${this.width}%; margin-left: ${this.startPosition}%`
    }

    get leftBorderStyle() {
        return `border-color: ${this.color}`
    }

    get leftBackgroundStyle() {
        return `background-color: ${this.color}`
    }
}
