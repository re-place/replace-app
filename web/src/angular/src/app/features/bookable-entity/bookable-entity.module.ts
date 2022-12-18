import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"

import { BookableEntityRoutingModule } from "./bookable-entity-routing.module"
import { BookableEntityComponent } from "./bookable-entity.component"

@NgModule({
    declarations: [BookableEntityComponent],
    imports: [CommonModule, BookableEntityRoutingModule],
})
export class BookableEntityModule {}
