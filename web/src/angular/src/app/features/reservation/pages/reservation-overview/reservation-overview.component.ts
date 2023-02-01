import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { BookingDto, DefaultService, FloorDto, SiteDto } from 'src/app/core/openapi';

@Component({
  selector: 'reservation-overview',
  templateUrl: './reservation-overview.component.html',
  styles: [
  ]
})
export class ReservationOverviewComponent implements OnInit {
    dataSource: MatTableDataSource<any> = new MatTableDataSource()
    displayedColumns = ["site", "floor", "entity", "start", "end", "actions"]

    bookings: BookingDto[] = []
    floors: FloorDto[] = []
    sites: SiteDto[] = []

    constructor(private apiService: DefaultService, private snackBar: MatSnackBar) { }

    ngOnInit(): void {
        this.getBookings()
    }

    getBookings() {
        const today = new Date()
        today.setUTCHours(0, 0, 0, 0)
        this.apiService.apiBookingByDateGet(today.toISOString()).subscribe({
            next: response => {
                this.bookings = response
                this.getSites()
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Buchungen konnten nicht abgefragt werden", "error", {duration: 5000}) 
            }
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
            }
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
            }
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
            row.floor = this.floors.find(floor => floor.id == entity.floorId)
            row.site = this.sites.find(site => site.id == row.floor.siteId)
            rows.push(row)
        })
        this.dataSource = new MatTableDataSource(rows)
    }

    deleteBooking(element: any) {
        console.log(element)
        this.apiService.apiBookingIdDelete(element.id).subscribe({
            next: response => {
                this.snackBar.open("Buchung gelöscht", "ok", {duration: 4000})
                this.getBookings()
            },
            error: err => {
                this.snackBar.open("Buchung konnte nicht gelöscht werden", "error", {duration: 5000})
            }
        })
    }

}
