import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { FormsModule } from "@angular/forms"

import { BookableEntityListComponent } from "./components/bookable-entity-list/bookable-entity-list.component"
// eslint-disable-next-line max-len
import { CreateOrUpdateBookableEntityComponent } from "./components/create-or-update-bookable-entity/create-or-update-bookable-entity.component"
import { FloorRoutingModule } from "./floor-routing.module"
import { EditComponent } from "./pages/edit/edit.component"
import { MaterialModule } from "src/app/material/material.module"
import { SharedModule } from "src/app/shared/shared.module"

@NgModule({
    declarations: [EditComponent, BookableEntityListComponent, CreateOrUpdateBookableEntityComponent],
    imports: [CommonModule, FloorRoutingModule, SharedModule, MaterialModule, FormsModule],
})
export class FloorModule {}
