import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { FormsModule } from "@angular/forms"
import { MatButtonModule } from "@angular/material/button"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatSelectModule } from "@angular/material/select"

import { ReservationComponent } from "./pages/reservation/reservation.component"
import { ReservationRoutingModule } from "./reservation-routing.module"


@NgModule({
    declarations: [
        ReservationComponent,
    ],
    imports: [
        CommonModule,
        ReservationRoutingModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        FormsModule,
        MatButtonModule,
    ],
})
export class ReservationModule { }
