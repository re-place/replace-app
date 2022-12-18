import { Component, OnDestroy } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"
import { SetOptional } from "type-fest"
import { Floor, Site } from "types"

import { ApiService } from "src/app/core/services/api.service"
import { DataLoader, Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<Site> | undefined = undefined
    floors = new DataLoader<Floor[]>()
    editingFloor: SetOptional<Floor, "id" | "siteId"> | undefined = undefined

    private readonly routeSub: Subscription

    constructor(
        private readonly api: ApiService,
        private readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.routeSub = route.params.subscribe(async (params) => {
            this.form = new Form(await api.getSite(params["id"]))
            this.form.useSnackbar(snackBar)
            this.title = `Gebäude ${this.form.data.name} bearbeiten`

            this.floors.source(() => api.getFloors(params["id"])).refresh()
        })
    }

    public async onSubmit() {
        this.form?.submit((data) => this.api.updateSite(data))
    }

    ngOnDestroy(): void {
        this.routeSub.unsubscribe()
    }

    public onCreateFloor() {
        this.editingFloor = { name: "" }
    }

    public onSubmitFloor(floor: SetOptional<Floor, "id" | "siteId">) {
        this.floors.loading(true)

        if (floor.id === undefined) {
            this.api
                .createFloor({
                    ...floor,
                    siteId: this.route.snapshot.params["id"],
                })
                .then(() => {
                    this.floors.refresh()
                    this.editingFloor = undefined
                })

            return
        }

        if (floor.id !== undefined) {
            this.api.updateFloor(floor as Floor).then(() => {
                this.floors.refresh()
                this.editingFloor = undefined
            })
        }
    }
}
