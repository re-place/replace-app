import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { RouterModule } from "@angular/router"
import * as FilePondPluginFileValidateType from "filepond-plugin-file-validate-type"
import FilePondPluginImagePreview from "filepond-plugin-image-preview"
import { FilePondModule, registerPlugin } from "ngx-filepond"

import { CardComponent } from "./components/card/card.component"
import { CrudCardComponent } from "./components/crud-card/crud-card.component"
import { CrudLayoutComponent } from "./components/crud-layout/crud-layout.component"
import { DataTableComponent } from "./components/data-table/data-table.component"
import { EmptyStateComponent } from "./components/empty-state/empty-state.component"
import { FileUploadComponent } from "./components/file-upload/file-upload.component"
import { LoadingStateComponent } from "./components/loading-state/loading-state.component"
import { TextButtonComponent } from "./components/text-button/text-button.component"
import { TimePickerComponent } from "./components/time-picker/time-picker.component"
import { UserCardComponent } from "./components/user-card/user-card.component"
import { ZoomControlComponent } from "./components/zoom-control/zoom-control.component"
import { ActionsDirective } from "./directives/actions.directive"
import { ExtraDirective } from "./directives/extra.directive"
import { FooterDirective } from "./directives/footer.directive"
import { HeaderDirective } from "./directives/header.directive"
import { IconDirective } from "./directives/icon.directive"
import { UserLayoutComponent } from "./layouts/user-layout/user-layout.component"
import { LocaleDateTimeRangePipe } from "./pipes/locale-date-time-range.pipe"
import { LocaleDateTimePipe } from "./pipes/locale-date-time.pipe"
import { LocaleDatePipe } from "./pipes/locale-date.pipe"
import { LocaleListPipe } from "./pipes/locale-list.pipe"
import { LocaleTimePipe } from "./pipes/locale-time.pipe"
import { MaterialModule } from "../material/material.module"
import { DragDropModule } from "@angular/cdk/drag-drop"

// Register the plugin
registerPlugin(FilePondPluginImagePreview)
// import filepond module

registerPlugin(FilePondPluginFileValidateType)

@NgModule({
    declarations: [
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
        FileUploadComponent,
        TimePickerComponent,
        ActionsDirective,
        IconDirective,
        LocaleDateTimePipe,
        LocaleDatePipe,
        LocaleTimePipe,
        LocaleListPipe,
        LocaleDateTimeRangePipe,
        TimePickerComponent,
        ZoomControlComponent,
    ],
    exports: [
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
        FileUploadComponent,
        TimePickerComponent,
        ActionsDirective,
        IconDirective,
        LocaleDateTimePipe,
        LocaleDatePipe,
        LocaleTimePipe,
        LocaleListPipe,
        LocaleDateTimeRangePipe,
        TimePickerComponent,
        ZoomControlComponent,
    ],
    imports: [CommonModule, MaterialModule, RouterModule, FilePondModule, DragDropModule],
})
export class SharedModule {}
