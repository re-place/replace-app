import { Component } from "@angular/core"

import { DefaultService, SiteDto } from "src/app/core/openapi"


@Component({
    selector: "index",
    templateUrl: "./index.component.html",
    styles: [],
})
export class IndexComponent {
    public sites: SiteDto[] | undefined

    public dataColumns = [
        { key: "id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public constructor(private readonly api: DefaultService) {
        api.apiSiteGet().subscribe((sites) => (this.sites = sites))
    }

    public getEditRoute(site: SiteDto) {
        return `./${site.id}/edit`
    }
}
