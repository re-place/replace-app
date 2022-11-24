import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";

import { AppComponent } from "./app.component";

import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MaterialModule } from "./material/material.module";
import { FormsModule } from "@angular/forms";

import { LoginComponent } from "./components/login/login.component";
import { AppRoutingModule } from "./app-routing.module";
import { UserLayoutComponent } from "./common/layouts/user-layout/user-layout.component";
import { AndrenaModule } from "./common/andrena/andrena.module";

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
