import { Component, EventEmitter, Input, Output } from "@angular/core"

import { Entity, EntityStatus } from "../entity-map/entity-map.component"

@Component({
    selector: "bookable-entities-list-item",
    templateUrl: "./bookable-entities-list-item.component.html",
    styles: [
    ],
})
export class BookableEntitiesListItemComponent {
    @Input() entity!: Entity

    @Output() entitySelected: EventEmitter<Entity> = new EventEmitter()

    get classes() {
        if (this.entity.status === EntityStatus.SELECTED) {
            return "shadow-primary-300 text-primary-500  cursor-pointer"
        }

        if (this.entity.status === EntityStatus.AVAILABLE) {
            return "hover:shadow-primary-300 hover:text-primary-500 cursor-pointer"
        }

        return "text-gray-300"
    }

    onClick() {
        if (this.entity.status === EntityStatus.DISABLED || this.entity.status === EntityStatus.BOOKED) {
            return
        }

        this.entitySelected.emit(this.entity)
    }
}
