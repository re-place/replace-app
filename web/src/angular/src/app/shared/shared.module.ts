import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UserCardComponent } from "./user-card/user-card.component";
import { UserLayoutComponent } from "./user-layout/user-layout.component";
import { AndrenaLogoComponent } from "./andrena-logo/andrena-logo.component";
import { MaterialModule } from "../material/material.module";
import { RouterModule } from "@angular/router";

@NgModule({
    declarations: [
        AndrenaLogoComponent,
        UserCardComponent,
        UserLayoutComponent,
    ],
    imports: [CommonModule, MaterialModule, RouterModule],
})
export class SharedModule {}
