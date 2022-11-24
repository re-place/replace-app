import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { UserLayoutComponent } from "./user-layout/user-layout.component";

const routes: Routes = [
    {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
    },
    {
        path: '',
        component: UserLayoutComponent,
        children: [
            {
                path: 'dashboard',
                loadChildren: () => import("./../../user/dashboard/dashboard.module").then(m => m.DashboardModule)
            },
            {
                path: 'reservation',
                loadChildren: () => import("./../../user/reservation/reservation.module").then(m => m.ReservationModule)
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule {
}
