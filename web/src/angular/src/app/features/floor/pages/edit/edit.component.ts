import { Component, OnDestroy } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"
import { SetOptional } from "type-fest"
import { BookableEntity, Floor } from "types"

import { ApiService } from "src/app/core/services/api.service"
import { DataLoader, Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<Floor> | undefined = undefined
    bookableEntities = new DataLoader<BookableEntity[]>()
    editingBookableEntity: SetOptional<BookableEntity, "id" | "parentId" | "floorId"> | undefined = undefined

    private readonly routeSub: Subscription

    constructor(
        private readonly api: ApiService,
        private readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.routeSub = route.params.subscribe(async (params) => {
            this.form = new Form(await api.getFloor(params["id"]))
            this.form.useSnackbar(snackBar)
            this.title = `Stockwerk ${this.form.data.name} bearbeiten`

            this.bookableEntities.source(() => api.getBookableEntities(params["id"])).refresh()
        })
    }

    public onSubmit() {
        this.form?.submit((data) => this.api.updateFloor(data))
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
}
