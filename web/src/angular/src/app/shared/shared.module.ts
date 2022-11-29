import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { RouterModule } from "@angular/router"

import { MaterialModule } from "../material/material.module"
import { AndrenaLogoComponent } from "./andrena-logo/andrena-logo.component"
import { UserCardComponent } from "./user-card/user-card.component"
import { UserLayoutComponent } from "./user-layout/user-layout.component"


@NgModule({
    declarations: [
        AndrenaLogoComponent,
        UserCardComponent,
        UserLayoutComponent,
    ],
    imports: [CommonModule, MaterialModule, RouterModule],
})
export class SharedModule {}
