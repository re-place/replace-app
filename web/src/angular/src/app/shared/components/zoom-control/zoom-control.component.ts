import { Component, Input } from "@angular/core"
import { Map as OlMap } from "ol"

@Component({
    selector: "zoom-control",
    templateUrl: "./zoom-control.component.html",
    styles: [],
})
export class ZoomControlComponent {

    @Input() map: OlMap | undefined

    onZoomIn() {
        const view = this.map?.getView()

        if (view === undefined) {
            return
        }

        view.setZoom((view.getZoom() ?? 1) + 1)
    }

    onZoomOut() {
        const view = this.map?.getView()

        if (view === undefined) {
            return
        }

        view.setZoom((view.getZoom() ?? 22) - 1)
    }
}
