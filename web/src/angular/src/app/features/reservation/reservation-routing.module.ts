import { NgModule } from "@angular/core"
import {MatNativeDateModule} from "@angular/material/core"
import {MatDatepickerModule} from "@angular/material/datepicker"
import { RouterModule, Routes } from "@angular/router"
import { ReservationOverviewComponent } from "./pages/reservation-overview/reservation-overview.component"

import { ReservationComponent } from "./pages/reservation/reservation.component"

const routes: Routes = [
    { path: "", component: ReservationComponent },
    { path: "my-bookings", component: ReservationOverviewComponent },
]

@NgModule({
    imports: [RouterModule.forChild(routes), MatDatepickerModule, MatNativeDateModule],
    exports: [RouterModule],
})
export class ReservationRoutingModule { }
