import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";

import { AppComponent } from "./app.component";
import { RouterModule } from "@angular/router";
import { LayoutsModule } from "./common/layouts/layouts.module";

@NgModule({
    declarations: [AppComponent],
    imports: [BrowserModule, RouterModule.forRoot([]), LayoutsModule],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
