import { Component } from "@angular/core"
import { Site } from "types"

import { ApiService } from "src/app/core/services/api.service"

@Component({
    selector: "index",
    templateUrl: "./index.component.html",
    styles: [],
})
export class IndexComponent {
    public sites: Site[] | undefined

    public dataColumns = [
        { key: "id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public constructor(private readonly api: ApiService) {
        api.getSites().then((sites) => (this.sites = sites))
    }

    public getEditRoute(site: Site) {
        return `./${site.id}/edit`
    }
}
