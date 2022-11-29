import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"

import { ReservationComponent } from "./pages/reservation/reservation.component"
import { ReservationRoutingModule } from "./reservation-routing.module"


@NgModule({
    declarations: [
        ReservationComponent,
    ],
    imports: [
        CommonModule,
        ReservationRoutingModule,
    ],
})
export class ReservationModule { }
