import { CommonModule, DatePipe } from "@angular/common"
import { NgModule } from "@angular/core"
import {FormsModule, ReactiveFormsModule} from "@angular/forms"
import { MatButtonModule } from "@angular/material/button"
import { MatCheckboxModule } from "@angular/material/checkbox"
import { MatDatepickerModule } from "@angular/material/datepicker"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatSelectModule } from "@angular/material/select"
import { MatSlideToggleModule } from "@angular/material/slide-toggle"

import { ReservationComponent } from "./pages/reservation/reservation.component"
import { ReservationOverviewComponent } from "./pages/reservation-overview/reservation-overview.component"
import { ReservationRoutingModule } from "./reservation-routing.module"
import { MaterialModule } from "../../material/material.module"


@NgModule({
    declarations: [
        ReservationComponent,
        ReservationOverviewComponent,
    ],
    imports: [
        CommonModule,
        ReservationRoutingModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        FormsModule,
        MatButtonModule,
        MatSlideToggleModule,
        MatDatepickerModule,
        ReactiveFormsModule,
        MaterialModule,
        MatCheckboxModule,
    ],
    providers: [
        DatePipe,
    ]
})
export class ReservationModule { }
