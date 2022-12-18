import { NgModule } from "@angular/core"
import { RouterModule, Routes } from "@angular/router"

import { EditComponent } from "./pages/edit/edit.component"

const routes: Routes = [{ path: ":id/edit", component: EditComponent }]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class FloorRoutingModule {}
