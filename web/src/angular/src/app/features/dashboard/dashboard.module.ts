import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"

import { DashboardRoutingModule } from "./dashboard-routing.module"
import { DashboardComponent } from "./pages/dashboard/dashboard.component"


@NgModule({
    declarations: [
        DashboardComponent,
    ],
    imports: [
        CommonModule,
        DashboardRoutingModule,
    ],
})
export class DashboardModule { }
