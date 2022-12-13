import { Component } from "@angular/core"
import { OfficeBuilding } from "types"

import { ApiService } from "src/app/core/services/api.service"

@Component({
    selector: "office-building",
    templateUrl: "./office-building.component.html",
    styles: [],
})
export class OfficeBuildingComponent {
    public officeBuildings: OfficeBuilding[] | undefined

    public dataColumns = [
        { key: "_id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public constructor(private readonly api: ApiService) {
        api.getOfficeBuildings().then((officeBuildings) => (this.officeBuildings = officeBuildings))
    }

    public getEditRoute(officeBuilding: OfficeBuilding) {
        return `./${officeBuilding._id}/edit`
    }
}
