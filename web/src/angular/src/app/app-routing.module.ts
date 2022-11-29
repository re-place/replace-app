import { NgModule } from "@angular/core"
import { RouterModule } from "@angular/router"

import { UserLayoutComponent } from "./shared/user-layout/user-layout.component"

import type { Routes } from "@angular/router"

const routes: Routes = [
    {
        path: "",
        redirectTo: "/dashboard",
        pathMatch: "full",
    },
    {
        path: "login",
        loadChildren: () => import("./features/login/login.module").then((m) => m.LoginModule),
    },
    {
        path: "",
        component: UserLayoutComponent,
        children: [
            {
                path: "dashboard",
                loadChildren: () => import("./features/dashboard/dashboard.module").then((m) => m.DashboardModule),
            },
            {
                path: "reservation",
                loadChildren: () => import("./features/reservation/reservation.module").then((m) => m.ReservationModule),
            },
        ],
    },
]

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
