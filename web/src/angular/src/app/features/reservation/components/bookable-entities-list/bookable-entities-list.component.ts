import { Component, EventEmitter, Input, Output } from "@angular/core"

import { Entity, EntityStatus } from "../entity-map/entity-map.component"

@Component({
    selector: "bookable-entities-list",
    templateUrl: "./bookable-entities-list.component.html",
    styles: [
    ],
})
export class BookableEntitiesListComponent {
    @Input() entities: Entity[] = []
    @Output() entitiesChange: EventEmitter<Entity[]> = new EventEmitter()

    get availableEntities() {
        return this.entities.filter(entity => entity.status === EntityStatus.AVAILABLE || entity.status === EntityStatus.SELECTED)
    }

    onEntitySelection(entity: Entity) {

        if (entity.status === EntityStatus.SELECTED) {
            entity.status = EntityStatus.AVAILABLE
        } else {
            entity.status = EntityStatus.SELECTED
        }

        this.entitiesChange.emit([...this.entities])
    }
}
