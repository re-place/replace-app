import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AdministrationComponent} from "./components/administration/administration.component";
import {BookingsComponent} from "./components/bookings/bookings.component";
import {LoginComponent} from "./components/login/login.component";
import {OverviewComponent} from "./components/overview/overview.component";

const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'overview', component: OverviewComponent },
    { path: 'bookings', component: BookingsComponent },
    { path: 'admin', component: AdministrationComponent },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
