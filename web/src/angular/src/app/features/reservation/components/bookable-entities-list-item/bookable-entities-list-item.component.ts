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
            return "shadow-primary-300 shadow-lg text-primary-500  cursor-pointer outline outline-primary-300"
        }

        if (this.entity.status === EntityStatus.AVAILABLE) {
            return "hover:shadow-primary-300 hover:text-primary-500 cursor-pointer shadow-md"
        }

        if (this.entity.status === EntityStatus.SELECTED_DISABLED) {
            return "text-gray-300 bg-gray-50 text-gray-500 shadow-primary-300 cursor-not-allowed shadow-md"
        }

        return "text-gray-300 bg-gray-50 text-gray-500 shadow-md"
    }

    onClick() {
        if (this.entity.status === EntityStatus.DISABLED || this.entity.status === EntityStatus.BOOKED) {
            return
        }

        this.entitySelected.emit(this.entity)
    }
}
