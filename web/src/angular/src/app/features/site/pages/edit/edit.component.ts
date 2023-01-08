import { Component, OnDestroy } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"

import { DefaultService, FloorDto, SiteDto } from "src/app/core/openapi"
import { DataLoader, Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<SiteDto> | undefined = undefined
    site: DataLoader<SiteDto> = new DataLoader<SiteDto>()
    floors = new DataLoader<FloorDto[]>()
    editingFloor: FloorDto | undefined = undefined

    private readonly routeSub: Subscription

    constructor(
        private readonly api: DefaultService,
        private readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.site.subscribe((site) => {
            this.form = new Form(site)
            this.form.useSnackbar(snackBar)
            this.title = `Gebäude ${this.form.data.name} bearbeiten`
        })

        this.routeSub = route.params.subscribe(async (params) => {
            this.site.source(() => api.apiSiteIdGet(params["id"])).refresh()
            this.floors.source(() => api.apiSiteSiteIdFloorGet(params["id"])).refresh()
        })
    }

    public onSubmit() {
        this.form?.submit((data) => this.api.apiSitePut(data), {
            onSuccess: () => {
                this.site.refresh()
            },
        })
    }

    ngOnDestroy(): void {
        this.routeSub.unsubscribe()
    }

    public onCreateFloor() {
        this.editingFloor = { name: "", planFile: undefined }
    }

    public onSubmitFloor(floor: FloorDto) {
        this.floors.loading(true)

        const form = new Form(floor)
        form.useSnackbar(this.snackBar)

        if (floor.id === undefined) {
            form.submit((data) => this.api.apiFloorPost({
                ...data,
                siteId: this.site.data?.id,
            }), {
                onSuccess: () => {
                    this.floors.refresh()
                    this.editingFloor = undefined
                },
            })

            return
        }
        form.submit((data) => this.api.apiFloorPut({
            ...data,
            siteId: this.site.data?.id,
        }), {
            onSuccess: () => {

                this.floors.refresh()
                this.editingFloor = undefined
            },
        })
    }
}
