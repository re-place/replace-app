import {Component, Input, OnInit} from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"

import { BookingDto, DefaultService, FloorDto, SiteDto } from "src/app/core/openapi"
import { toLocaleDateTimeString } from "src/app/util/DateTime"

@Component({
    selector: "reservation-overview",
    templateUrl: "./reservation-overview.component.html",
    styles: [],
})
export class ReservationOverviewComponent implements OnInit {
    bookings: BookingDto[] = []
    floors: FloorDto[] = []
    sites: SiteDto[] = []

    tableData: any[] = []

    public dataColumns = [
        { key: "site", label: "Standort", getter: (booking: any) => booking.site.name },
        { key: "floor", label: "Stockwerk", getter: (booking: any) => booking.floor.name },
        { key: "entities", label: "Objekte", getter: (booking: any) => booking.entities },
        { key: "start", label: "Anfang", getter: (booking: any) => toLocaleDateTimeString(booking.start) },
        { key: "end", label: "Ende", getter: (booking: any) => toLocaleDateTimeString(booking.end) },
    ]

    constructor(private readonly apiService: DefaultService, private readonly snackBar: MatSnackBar) { }

    ngOnInit(): void {
        this.getBookings()
    }

    getBookings() {
        const today = new Date()
        today.setHours(0, 0, 0, 0)
        this.apiService.apiBookingByParamsGet(today.toISOString()).subscribe({
            next: response => {
                this.bookings = response
                this.getSites()
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Buchungen konnten nicht abgefragt werden", "error", {duration: 5000})
            },
        })
    }

    getSites() {
        this.apiService.apiSiteGet().subscribe({
            next: response => {
                this.sites = response
                this.getFloors()
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Stockwerke konnten nicht abgefragt werden", "error", {duration: 5000})
            },
        })
    }

    getFloors() {
        this.apiService.apiFloorGet().subscribe({
            next: response => {
                this.floors = response
                this.enrichBookings()
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Stockwerke konnten nicht abgefragt werden", "error", {duration: 5000})
            },
        })
    }

    enrichBookings() {
        const rows: any[] = []
        this.bookings.forEach(booking => {
            const row: any = {...booking}
            if(booking.bookedEntities == undefined || booking.bookedEntities.length < 1)
                return
            const entity = booking.bookedEntities[0]
            row.entities = booking.bookedEntities.map(ent => ent.name).join(", ")
            const floor = this.floors.find(floor => floor.id == entity.floorId)
            row.floor = floor?.name
            row.site = this.sites.find(site => site.id == floor?.siteId)?.name
            rows.push(row)
        })

        this.tableData = rows
    }

    deleteBooking(element: any) {
        this.apiService.apiBookingIdDelete(element.id).subscribe({
            next: response => {
                this.snackBar.open("Buchung gelöscht", "ok", {duration: 4000})
                this.getBookings()
            },
            error: err => {
                this.snackBar.open("Buchung konnte nicht gelöscht werden", "error", {duration: 5000})
            },
        })
    }
}
