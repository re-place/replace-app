import { NgModule } from "@angular/core"
import { RouterModule } from "@angular/router"

import { AuthGuard } from "./core/guards/auth.guard"
import { UserLayoutComponent } from "./shared/layouts/user-layout/user-layout.component"

import type { Routes } from "@angular/router"

const routes: Routes = [
    // {
    //     path: "login",
    //     loadChildren: () => import("./features/login/login.module").then((m) => m.LoginModule),
    // },
    {
        path: "",
        redirectTo: "/reservation/my-bookings",
        pathMatch: "full",
    },
    {
        path: "",
        component: UserLayoutComponent,
        children: [
            {
                path: "reservation",
                loadChildren: () => import("./features/reservation/reservation.module").then((m) => m.ReservationModule),
            },
            {
                path: "site",
                loadChildren: () => import("./features/site/site.module").then((m) => m.SiteModule),
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
