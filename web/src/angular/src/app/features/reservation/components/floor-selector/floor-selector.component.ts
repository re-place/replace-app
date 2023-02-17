import { Component, EventEmitter, Input, Output } from "@angular/core"

import { FloorDto } from "src/app/core/openapi"

@Component({
    selector: "floor-selector",
    templateUrl: "./floor-selector.component.html",
    styles: [
    ],
})
export class FloorSelectorComponent {

    @Input() floors: FloorDto[] = []

    @Output() floorSelected: EventEmitter<FloorDto> = new EventEmitter()

    onFloorSelection(site: FloorDto) {
        this.floorSelected.emit(site)
    }
}
