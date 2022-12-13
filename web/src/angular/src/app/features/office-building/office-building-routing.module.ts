import { NgModule } from "@angular/core"
import { RouterModule, Routes } from "@angular/router"

import { CreateComponent } from "./pages/create/create.component"
import { EditComponent } from "./pages/edit/edit.component"
import { IndexComponent } from "./pages/index/index.component"

const routes: Routes = [
    { path: "", component: IndexComponent },
    { path: "create", component: CreateComponent },
    { path: ":id/edit", component: EditComponent },
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class OfficeBuildingRoutingModule {}
