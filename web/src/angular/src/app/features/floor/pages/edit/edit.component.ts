import { Component, OnDestroy } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"
import { SetOptional } from "type-fest"
import { BookableEntity, Floor } from "types"

import { ApiService } from "src/app/core/services/api.service"
import { FileUpload } from "src/app/shared/components/file-upload/file-upload.component"
import { DataLoader, Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<Floor> | undefined = undefined
    floor = new DataLoader<Floor>()
    bookableEntities = new DataLoader<BookableEntity[]>()
    editingBookableEntity: SetOptional<BookableEntity, "id" | "parentId" | "floorId"> | undefined = undefined

    private readonly routeSub: Subscription

    constructor(
        private readonly api: ApiService,
        private readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.floor.subscribe((floor) => {
            this.form = new Form(floor)
            this.form.useSnackbar(snackBar)
            this.title = `Stockwerk ${this.form.data.name} bearbeiten`
        })

        this.routeSub = route.params.subscribe(async (params) => {
            this.floor.source(() => api.getFloor(params["id"])).refresh()
            this.bookableEntities.source(() => api.getBookableEntities(params["id"])).refresh()
        })
    }

    public get files(): FileUpload[] {
        const planFile = this.form?.data.planFile

        if (planFile === undefined || planFile === null) {
            return []
        }

        return [planFile]
    }

    public set files(newFiles: FileUpload[]) {
        if (this.form === undefined) {
            return
        }

        this.form.data.planFile = newFiles.at(0) ?? null
    }

    public async onSubmit() {
        await this.form?.submit((data) => this.api.updateFloor(data))
        this.floor.refresh()
    }

    ngOnDestroy(): void {
        this.routeSub.unsubscribe()
    }

    public onEditBookableEntity(bookableEntity?: BookableEntity) {
        this.editingBookableEntity = bookableEntity
    }

    public onCreateBookableEntity() {
        this.editingBookableEntity = { name: "", type: null }
    }

    public onSubmitBookableEntity(bookableEntity: SetOptional<BookableEntity, "id" | "floorId" | "parentId">) {
        this.bookableEntities.loading(true)

        if (bookableEntity.id === undefined) {
            this.api
                .createBookableEntity({
                    ...bookableEntity,
                    floorId: this.route.snapshot.params["id"],
                    parentId: null,
                })
                .then(() => {
                    this.bookableEntities.refresh()
                    this.editingBookableEntity = undefined
                })

            return
        }

        if (bookableEntity.id !== undefined) {
            this.api.updateBookableEntity(bookableEntity as BookableEntity).then(() => {
                this.bookableEntities.refresh()
                this.editingBookableEntity = undefined
            })
        }
    }

    public get initialFiles(): string[] {
        const planFile = this.form?.data.planFile

        if (planFile === undefined || planFile === null || planFile.temporary) {
            return []
        }

        return [planFile.id]
    }

    public onFilesUploaded(files: FileUpload[]) {
        this.files = files
    }
}
