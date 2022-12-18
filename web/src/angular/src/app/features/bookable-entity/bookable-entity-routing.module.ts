import { NgModule } from "@angular/core"
import { RouterModule, Routes } from "@angular/router"

import { BookableEntityComponent } from "./bookable-entity.component"

const routes: Routes = [{ path: "", component: BookableEntityComponent }]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class BookableEntityRoutingModule {}
