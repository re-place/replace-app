import { Component, OnDestroy } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"
import { SetOptional } from "type-fest"

import { BookableEntityDto, DefaultService, FileUploadDto, FloorDto } from "src/app/core/openapi"
import { DataLoader, Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<FloorDto> | undefined = undefined
    floor = new DataLoader<FloorDto>()
    bookableEntities = new DataLoader<BookableEntityDto[]>()
    editingBookableEntity: SetOptional<BookableEntityDto, "id" | "parentId" | "floorId"> | undefined = undefined

    private readonly routeSub: Subscription

    constructor(
        private readonly api: DefaultService,
        private readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.floor.subscribe((floor) => {
            this.form = new Form(floor)
            this.form.useSnackbar(snackBar)
            this.title = `Stockwerk ${this.form.data.name} bearbeiten`
        })

        this.routeSub = route.params.subscribe(async (params) => {
            this.floor.source(() => api.apiFloorIdGet(params["id"])).refresh()
            this.bookableEntities.source(() => api.apiFloorFloorIdBookableEntityGet(params["id"])).refresh()
        })
    }

    public get files(): FileUploadDto[] {
        const planFile = this.form?.data.planFile

        if (planFile === undefined || planFile === null) {
            return []
        }

        return [planFile]
    }

    public set files(newFiles: FileUploadDto[]) {
        if (this.form === undefined) {
            return
        }

        this.form.data.planFile = newFiles.at(0) ?? undefined
    }

    public async onSubmit() {
        await this.form?.submit((data) => this.api.apiFloorPut(data))
        this.floor.refresh()
    }

    ngOnDestroy(): void {
        this.routeSub.unsubscribe()
    }

    public onEditBookableEntity(bookableEntity?: BookableEntityDto) {
        this.editingBookableEntity = bookableEntity
    }

    public onCreateBookableEntity() {
        this.editingBookableEntity = { name: "", type: undefined }
    }

    public onSubmitBookableEntity(bookableEntity: BookableEntityDto) {
        this.bookableEntities.loading(true)

        const form = new Form(bookableEntity)
        form.useSnackbar(this.snackBar)

        if (bookableEntity.id === undefined) {
            form.submit((data) => this.api.apiBookableEntityPost({
                ...data,
                floorId: this.floor.data?.id,
            })).then(() => {
                this.bookableEntities.refresh()
                this.editingBookableEntity = undefined
            })

            return
        }

        if (bookableEntity.id !== undefined) {
            form.submit((data) => this.api.apiBookableEntityPut({
                ...data,
                floorId: this.floor.data?.id,
            })).then(() => {
                this.bookableEntities.refresh()
                this.editingBookableEntity = undefined
            })
        }
    }

    public get initialFiles(): string[] {
        const planFile = this.form?.data.planFile

        if (planFile === undefined || planFile === null || planFile.temporary === true || planFile.id === undefined) {
            return []
        }

        return [planFile.id]
    }

    public onFilesUploaded(files: FileUploadDto[]) {
        this.files = files
    }
}
