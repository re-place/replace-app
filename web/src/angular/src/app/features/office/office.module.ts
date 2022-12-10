import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { FormsModule, ReactiveFormsModule } from "@angular/forms"

import { OfficeRoutingModule } from "./office-routing.module"
import { CreateComponent } from "./pages/create/create.component"
import { EditComponent } from "./pages/edit/edit.component"
import { OfficeComponent } from "./pages/office.component"
import { MaterialModule } from "src/app/material/material.module"
import { SharedModule } from "src/app/shared/shared.module"

@NgModule({
    declarations: [OfficeComponent, CreateComponent, EditComponent],
    imports: [CommonModule, OfficeRoutingModule, SharedModule, MaterialModule, FormsModule, ReactiveFormsModule],
})
export class OfficeModule {}
