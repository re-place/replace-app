import {Component, OnInit} from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"

import { BookingDto, DefaultService, FloorDto, SiteDto } from "src/app/core/openapi"
import { BookableEntities } from "src/app/core/openapi/model/bookableEntities"

type Booking = {
    id: string
    start: Date
    end: Date

    site: SiteDto
    floor: FloorDto

    bookedEntities: BookableEntities[]
}

@Component({
    selector: "reservation-overview",
    templateUrl: "./reservation-overview.component.html",
    styles: [],
})
export class ReservationOverviewComponent implements OnInit {
    loadedBookings: BookingDto[] | undefined = undefined
    bookings: Booking[] = []
    floors: Map<string, FloorDto> | undefined = undefined
    sites: Map<string, SiteDto> | undefined = undefined

    groupedBookings: { start: Date, bookings: Booking[] }[] = []

    dateFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "full",
    }

    dateTimeFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

    constructor(private readonly apiService: DefaultService, private readonly snackBar: MatSnackBar) { }

    ngOnInit(): void {
        this.loadBookings()
        this.loadSites()
        this.loadFloors()
    }

    toBookings(bookings: BookingDto[]): Booking[] {
        if (this.sites === undefined || this.floors === undefined) {
            return []
        }

        return bookings.map(booking => {
            const floor = this.floors?.get(booking.bookedEntities?.at(0)?.floorId ?? "")

            if (floor === undefined) {
                console.error("Floor not found", booking.bookedEntities?.at(0)?.floorId)
                return undefined
            }

            const site = this.sites?.get(floor.siteId as string)

            if (site === undefined) {
                console.error("Site not found", floor.siteId)
                return undefined
            }

            return {
                id: booking.id as string,
                start: new Date(booking.start as string),
                end: new Date(booking.end as string),
                site,
                floor,
                bookedEntities: booking.bookedEntities as BookableEntities[],
            }
        })
            .filter((booking): booking is Exclude<typeof booking, undefined> => booking !== undefined)
    }

    transformBookings() {
        if (this.loadedBookings === undefined) {
            return
        }

        this.bookings = this.toBookings(this.loadedBookings)
        this.groupedBookings = this.groupBookings(this.bookings)
    }

    loadBookings() {
        this.loadedBookings = undefined
        const today = new Date()
        today.setHours(0, 0, 0, 0)
        this.apiService.apiBookingByParamsGet(today.toISOString(), undefined, undefined, undefined, true).subscribe({
            next: response => {
                this.loadedBookings = response
                this.transformBookings()
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Buchungen konnten nicht abgefragt werden", "error", {duration: 5000})
            },
        })
    }

    loadSites() {
        this.sites = undefined
        this.apiService.apiSiteGet().subscribe({
            next: response => {
                this.sites = new Map(response.map(site => [site.id as string, site]))
                this.transformBookings()
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Stockwerke konnten nicht abgefragt werden", "error", {duration: 5000})
            },
        })
    }

    loadFloors() {
        this.floors = undefined
        this.apiService.apiFloorGet().subscribe({
            next: response => {
                this.floors = new Map(response.map(floor => [floor.id as string, floor]))
                this.transformBookings()
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Stockwerke konnten nicht abgefragt werden", "error", {duration: 5000})
            },
        })
    }

    groupBookings(bookings: Booking[]): { start: Date, bookings: Booking[] }[] {
        const groupedBookings = new Map<number, Booking[]>()

        bookings.forEach(booking => {
            const start = new Date(booking.start)
            start.setHours(0, 0, 0, 0)

            const key = start.getTime()

            if (groupedBookings.has(key)) {
                groupedBookings.get(key)?.push(booking)
            } else {
                groupedBookings.set(key, [booking])
            }
        })

        const grouped = Array.from(groupedBookings.entries())

        grouped.sort((a, b) => a[0] - b[0])
        grouped.forEach(group => group[1].sort((a, b) => a.start.getTime() - b.start.getTime()))

        return grouped.map(group => ({
            start: new Date(group[0]),
            bookings: group[1],
        }))
    }

    deleteBooking(element: BookingDto) {
        this.apiService.apiBookingIdDelete(element.id as string).subscribe({
            next: response => {
                this.snackBar.open("Buchung gelöscht", "ok", {duration: 4000})
                this.loadBookings()
            },
            error: err => {
                this.snackBar.open("Buchung konnte nicht gelöscht werden", "error", {duration: 5000})
            },
        })
    }
}
