import { Component, EventEmitter, Input, Output } from "@angular/core"

import { SiteDto } from "src/app/core/openapi"

@Component({
    selector: "site-selector",
    templateUrl: "./site-selector.component.html",
    styles: [
    ],
})
export class SiteSelectorComponent {

    @Input() sites: SiteDto[] = []

    @Output() siteSelected: EventEmitter<SiteDto> = new EventEmitter()

    onSiteSelection(site: SiteDto) {
        this.siteSelected.emit(site)
    }
}
