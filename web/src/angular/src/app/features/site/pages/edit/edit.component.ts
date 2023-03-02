import { Component, OnDestroy } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"

import { BookableEntityDto, CreateFloorDto, DefaultService, FloorDto, SiteDto, UpdateFloorDto, UpdateSiteDto } from "src/app/core/openapi"
import { BookableEntities } from "src/app/core/openapi/model/bookableEntities"
import { BookableEntity } from "src/app/core/openapi/model/bookableEntity"
import { DataLoader, Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<UpdateSiteDto> | undefined = undefined
    site: DataLoader<SiteDto> = new DataLoader<SiteDto>()
    floors = new DataLoader<FloorDto[]>()
    editingFloor: CreateFloorDto | UpdateFloorDto | undefined = undefined
    floor = new DataLoader<FloorDto>()
    bookableEntities = new DataLoader<BookableEntityDto[]>

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
            this.bookableEntities.source(() => api.apiBookableEntityGet(undefined, false, undefined)).refresh()
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

    public onSubmitFloor(floor: CreateFloorDto | UpdateFloorDto) {
        this.floors.loading(true)

        const form = new Form(floor)
        form.useSnackbar(this.snackBar)

        if ((floor as UpdateFloorDto).id === undefined) {
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

    public onDeleteFloor(id: string) {
       
        this.api.apiFloorIdDelete(id).subscribe({
            next: () => {
                if (this.floor?.data?.id === id) {
                    this.floor.data.id = undefined
                }
                this.floors.refresh()
                this.snackBar.open("Erfolgreich gelöscht", "OK", { duration: 1000 })
            },
            error: (error) => {
                this.snackBar.open(error.message, "OK")
            },
        })
    }
}
