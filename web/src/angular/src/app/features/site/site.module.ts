import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { FormsModule, ReactiveFormsModule } from "@angular/forms"

import { CreateOrUpdateFloorComponent } from "./components/create-or-update-floor/create-or-update-floor.component"
import { FloorListComponent } from "./components/floor-list/floor-list.component"
import { CreateComponent } from "./pages/create/create.component"
import { EditComponent } from "./pages/edit/edit.component"
import { IndexComponent } from "./pages/index/index.component"
import { SiteRoutingModule } from "./site-routing.module"
import { MaterialModule } from "src/app/material/material.module"
import { SharedModule } from "src/app/shared/shared.module"

@NgModule({
    declarations: [IndexComponent, CreateComponent, EditComponent, CreateOrUpdateFloorComponent, FloorListComponent],
    imports: [CommonModule, SiteRoutingModule, SharedModule, MaterialModule, FormsModule, ReactiveFormsModule],
})
export class SiteModule {}
