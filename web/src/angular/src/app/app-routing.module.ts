import { DashboardComponent } from "src/app/components/dashboard/dashboard.component"
import { NgModule } from "@angular/core"
import { LoginComponent } from "src/app/components/login/login.component"
import { ReservationComponent } from "src/app/components/reservation/reservation.component"
import { UserLayoutComponent } from "./common/layouts/user-layout/user-layout.component"
import { RouterModule, Routes } from "@angular/router"

const routes: Routes = [
    {
        path: "",
        redirectTo: "/login",
        pathMatch: "full",
    },
    {
        path: "login",
        component: LoginComponent,
    },
    {
        path: "",
        component: UserLayoutComponent,
        children: [
            {
                path: "dashboard",
                component: DashboardComponent,
            },
            {
                path: "reservation",
                component: ReservationComponent,
            },
        ],
    },
]

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
