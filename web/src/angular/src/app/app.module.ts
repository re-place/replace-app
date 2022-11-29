import { NgModule } from "@angular/core"
import { FormsModule } from "@angular/forms"
import { BrowserModule } from "@angular/platform-browser"
import { BrowserAnimationsModule } from "@angular/platform-browser/animations"

import { AppRoutingModule } from "./app-routing.module"
import { AppComponent } from "./app.component"
import { AndrenaModule } from "./common/andrena/andrena.module"
import { UserLayoutComponent } from "./common/layouts/user-layout/user-layout.component"
import { LoginComponent } from "./components/login/login.component"
import { MaterialModule } from "./material/material.module"

@NgModule({
    declarations: [AppComponent, LoginComponent, UserLayoutComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MaterialModule,
        FormsModule,
        AndrenaModule,
    ],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
