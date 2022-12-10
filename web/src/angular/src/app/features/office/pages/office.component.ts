import { Component } from "@angular/core"
import { Office } from "types"

import { ApiService } from "src/app/core/services/api.service"

@Component({
    selector: "office",
    templateUrl: "./office.component.html",
    styles: [],
})
export class OfficeComponent {
    public offices: Office[] | undefined

    public dataColumns = [
        { key: "_id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public constructor(private readonly api: ApiService) {
        api.getOffices().then((offices) => (this.offices = offices))
    }

    public getEditRoute(office: Office) {
        return `./${office._id}/edit`
    }
}
