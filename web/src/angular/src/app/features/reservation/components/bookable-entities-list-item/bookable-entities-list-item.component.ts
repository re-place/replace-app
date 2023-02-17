import { Component, EventEmitter, Input, Output } from "@angular/core"

import { Entity } from "../entity-map/entity-map.component"

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
        if (this.entity.selected) {
            return "shadow-primary-300 text-primary-500  cursor-pointer"
        }

        if (this.entity.available) {
            return "hover:shadow-primary-300 hover:text-primary-500 cursor-pointer"
        }

        return "text-gray-300"
    }

    onClick() {
        if (!this.entity.available) {
            return
        }

        this.entitySelected.emit(this.entity)
    }
}
