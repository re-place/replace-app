import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import {FormsModule, ReactiveFormsModule} from "@angular/forms"
import { MatButtonModule } from "@angular/material/button"
import { MatDatepickerModule } from "@angular/material/datepicker"
import { MatCheckboxModule } from "@angular/material/checkbox"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatSelectModule } from "@angular/material/select"
import { MatSlideToggleModule } from "@angular/material/slide-toggle"

import { MaterialModule } from "../../material/material.module"
import { ReservationComponent } from "./pages/reservation/reservation.component"
import { ReservationRoutingModule } from "./reservation-routing.module";
import { ReservationOverviewComponent } from './pages/reservation-overview/reservation-overview.component'


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
})
export class ReservationModule { }
