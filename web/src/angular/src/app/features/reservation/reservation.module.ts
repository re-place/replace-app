import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import {FormsModule, ReactiveFormsModule} from "@angular/forms"
import { MatButtonModule } from "@angular/material/button"
import { MatCheckboxModule } from "@angular/material/checkbox"
import { MatDatepickerModule } from "@angular/material/datepicker"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatSelectModule } from "@angular/material/select"
import { MatSlideToggleModule } from "@angular/material/slide-toggle"

import { BookableEntitiesListComponent } from "./components/bookable-entities-list/bookable-entities-list.component"
import { BookableEntitiesListItemComponent } from "./components/bookable-entities-list-item/bookable-entities-list-item.component"
import { BookingsListComponent } from "./components/bookings-list/bookings-list.component"
import { BookingsListItemComponent } from "./components/bookings-list-item/bookings-list-item.component"
import { EntityMapComponent } from "./components/entity-map/entity-map.component"
import { FloorSelectorComponent } from "./components/floor-selector/floor-selector.component"
import { SiteSelectorComponent } from "./components/site-selector/site-selector.component"
import { TimeSelectorComponent } from "./components/time-selector/time-selector.component"
import { ReservationComponent } from "./pages/reservation/reservation.component"
import { ReservationOverviewComponent } from "./pages/reservation-overview/reservation-overview.component"
import { ReservationRoutingModule } from "./reservation-routing.module"
import { MaterialModule } from "../../material/material.module"
import { SharedModule } from "src/app/shared/shared.module"


@NgModule({
    declarations: [
        ReservationComponent,
        ReservationOverviewComponent,
        EntityMapComponent,
        TimeSelectorComponent,
        SiteSelectorComponent,
        FloorSelectorComponent,
        BookingsListComponent,
        BookingsListItemComponent,
        BookableEntitiesListComponent,
        BookableEntitiesListItemComponent,
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
        SharedModule,
    ],
})
export class ReservationModule { }
