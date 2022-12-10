import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { RouterModule } from "@angular/router"

import { MaterialModule } from "../material/material.module"
import { AndrenaLogoComponent } from "./components/andrena-logo/andrena-logo.component"
import { CardComponent } from "./components/card/card.component"
import { CrudCardComponent } from "./components/crud-card/crud-card.component"
import { CrudLayoutComponent } from "./components/crud-layout/crud-layout.component"
import { DataTableComponent } from "./components/data-table/data-table.component"
import { LoadingStateComponent } from "./components/loading-state/loading-state.component"
import { UserCardComponent } from "./components/user-card/user-card.component"
import { ExtraDirective } from "./directives/extra.directive"
import { FooterDirective } from "./directives/footer.directive"
import { HeaderDirective } from "./directives/header.directive"
import { UserLayoutComponent } from "./layouts/user-layout/user-layout.component"

@NgModule({
    declarations: [
        AndrenaLogoComponent,
        UserCardComponent,
        UserLayoutComponent,
        CrudLayoutComponent,
        CardComponent,
        LoadingStateComponent,
        DataTableComponent,
        CrudCardComponent,
        HeaderDirective,
        FooterDirective,
        ExtraDirective,
    ],
    exports: [
        AndrenaLogoComponent,
        UserCardComponent,
        UserLayoutComponent,
        CrudLayoutComponent,
        CardComponent,
        LoadingStateComponent,
        DataTableComponent,
        CrudCardComponent,
        HeaderDirective,
        FooterDirective,
        ExtraDirective,
    ],
    imports: [CommonModule, MaterialModule, RouterModule],
})
export class SharedModule {}
