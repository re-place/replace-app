import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { FormsModule, ReactiveFormsModule } from "@angular/forms"

import { CreateOrUpdateFloorComponent } from "./components/create-or-update-floor/create-or-update-floor.component"
import { FloorListComponent } from "./components/floor-list/floor-list.component"
import { OfficeBuildingRoutingModule } from "./office-building-routing.module"
import { CreateComponent } from "./pages/create/create.component"
import { EditComponent } from "./pages/edit/edit.component"
import { IndexComponent } from "./pages/index/index.component"
import { MaterialModule } from "src/app/material/material.module"
import { SharedModule } from "src/app/shared/shared.module"

@NgModule({
    declarations: [IndexComponent, CreateComponent, EditComponent, CreateOrUpdateFloorComponent, FloorListComponent],
    imports: [CommonModule, OfficeBuildingRoutingModule, SharedModule, MaterialModule, FormsModule, ReactiveFormsModule],
})
export class OfficeBuildingModule {}
