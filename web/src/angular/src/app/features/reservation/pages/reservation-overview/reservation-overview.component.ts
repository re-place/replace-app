import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BookingDto, DefaultService } from 'src/app/core/openapi';

@Component({
  selector: 'reservation-overview',
  templateUrl: './reservation-overview.component.html',
  styles: [
  ]
})
export class ReservationOverviewComponent implements OnInit {

    bookings: BookingDto[] = []

    constructor(private apiService: DefaultService, private snackBar: MatSnackBar) { }

    ngOnInit(): void {
        this.getBookings()
    }

    getBookings() {
        const today = new Date()
        today.setUTCHours(0, 0, 0, 0)
        this.apiService.apiBookingByDateGet(today.toISOString()).subscribe({
            next: response => {
                console.log(response)
                this.bookings = response
            },
            error: err => {
                console.log(err)
                this.snackBar.open("Buchungen konnten nicht abgefragt werden", "error", {duration: 5000}) 
            }
        })
    }

}
