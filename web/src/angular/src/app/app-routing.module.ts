import { NgModule } from "@angular/core"
import { RouterModule } from "@angular/router"

import { AuthGuard } from "./core/guards/auth.guard"
import { UserLayoutComponent } from "./shared/layouts/user-layout/user-layout.component"

import type { Routes } from "@angular/router"

const routes: Routes = [
    {
        path: "login",
        loadChildren: () => import("./features/login/login.module").then((m) => m.LoginModule),
    },
    {
        path: "",
        redirectTo: "/dashboard",
        pathMatch: "full",
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
            {
                path: "office-building",
                loadChildren: () =>
                    import("./features/office-building/office-building.module").then((m) => m.OfficeBuildingModule),
            },
            { path: "floor", loadChildren: () => import("./features/floor/floor.module").then((m) => m.FloorModule) },
            {
                path: "bookable-entity",
                loadChildren: () =>
                    import("./features/bookable-entity/bookable-entity.module").then((m) => m.BookableEntityModule),
            },
        ],
        canActivate: [AuthGuard],
    },
]

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
