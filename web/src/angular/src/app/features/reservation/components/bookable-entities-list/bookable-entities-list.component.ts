import { Component, EventEmitter, Input, Output } from "@angular/core"

import { Entity } from "../entity-map/entity-map.component"

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
        return this.entities.filter(entity => entity.available)
    }

    onEntitySelection(entity: Entity) {
        const index = this.entities.findIndex(e => e.entity.id === entity.entity.id)

        if (index === -1) {
            return
        }

        this.entities[index].selected = !this.entities[index].selected

        this.entitiesChange.emit([...this.entities])
    }
}
