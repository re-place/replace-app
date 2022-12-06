import { NgModule } from "@angular/core"
import { FormsModule } from "@angular/forms"
import { BrowserModule } from "@angular/platform-browser"
import { BrowserAnimationsModule } from "@angular/platform-browser/animations"

import { AppRoutingModule } from "./app-routing.module"
import { AppComponent } from "./app.component"
import { CoreModule } from "./core/core.module"
import { MaterialModule } from "./material/material.module"
import { SharedModule } from "./shared/shared.module"

@NgModule({
    declarations: [AppComponent],
    imports: [BrowserModule, AppRoutingModule, BrowserAnimationsModule, MaterialModule, FormsModule, SharedModule, CoreModule],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
