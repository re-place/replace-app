import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { RouterModule } from "@angular/router"
import * as FilePondPluginFileValidateType from "filepond-plugin-file-validate-type"
import { FilePondModule, registerPlugin } from "ngx-filepond"

import { MaterialModule } from "../material/material.module"
import { AndrenaLogoComponent } from "./components/andrena-logo/andrena-logo.component"
import { CardComponent } from "./components/card/card.component"
import { CrudCardComponent } from "./components/crud-card/crud-card.component"
import { CrudLayoutComponent } from "./components/crud-layout/crud-layout.component"
import { DataTableComponent } from "./components/data-table/data-table.component"
import { EmptyStateComponent } from "./components/empty-state/empty-state.component"
import { FileUploadComponent } from "./components/file-upload/file-upload.component"
import { LoadingStateComponent } from "./components/loading-state/loading-state.component"
import { TextButtonComponent } from "./components/text-button/text-button.component"
import { UserCardComponent } from "./components/user-card/user-card.component"
import { ActionsDirective } from "./directives/actions.directive"
import { ExtraDirective } from "./directives/extra.directive"
import { FooterDirective } from "./directives/footer.directive"
import { HeaderDirective } from "./directives/header.directive"
import { IconDirective } from "./directives/icon.directive"
import { UserLayoutComponent } from "./layouts/user-layout/user-layout.component"

// import filepond module

registerPlugin(FilePondPluginFileValidateType)

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
        TextButtonComponent,
        EmptyStateComponent,
        ActionsDirective,
        IconDirective,
        FileUploadComponent,
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
        TextButtonComponent,
        EmptyStateComponent,
        ActionsDirective,
        IconDirective,
        FileUploadComponent,
    ],
    imports: [CommonModule, MaterialModule, RouterModule, FilePondModule],
})
export class SharedModule {}
