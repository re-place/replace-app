import { Component, EventEmitter, Input, Output } from "@angular/core"

import { Entity, EntityStatus, interactableStates } from "../entity-map/entity-map.component"

@Component({
    selector: "bookable-entities-list",
    templateUrl: "./bookable-entities-list.component.html",
    styles: [
    ],
})
export class BookableEntitiesListComponent {
    @Input() entities: Entity[] = []
    @Output() entitySelected: EventEmitter<Entity> = new EventEmitter()

    get availableEntities() {
        return this.entities.filter(
            entity => entity.status === EntityStatus.AVAILABLE ||
            entity.status === EntityStatus.SELECTED ||
            entity.status === EntityStatus.SELECTED_DISABLED,
        )
    }

    onEntitySelection(entity: Entity) {
        if (!interactableStates.includes(entity.status)) {
            return
        }

        this.entitySelected.emit(entity)
    }
}
