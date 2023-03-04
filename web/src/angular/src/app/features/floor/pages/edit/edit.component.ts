import { Component, OnDestroy } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"
import { SetOptional } from "type-fest"

import { BookableEntityDto, BookableEntityTypeDto, DefaultService, FileUploadDto, FloorDto, UpdateFloorDto } from "src/app/core/openapi"
import { DataLoader, Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<UpdateFloorDto> | undefined = undefined
    floor = new DataLoader<FloorDto>()
    bookableEntities = new DataLoader<BookableEntityDto[]>()
    types: BookableEntityTypeDto[] = []
    editingBookableEntity: SetOptional<BookableEntityDto, "id" | "parentId" | "floorId"> | undefined = undefined

    private readonly routeSub: Subscription

    constructor(
        private readonly api: DefaultService,
        private readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.api.apiBookableEntityTypeGet().subscribe({
            next: result => {
                this.types = result
            },
        })
        this.floor.subscribe((floor) => {
            this.form = new Form({
                id: floor.id,
                name: floor.name,
                planFile: floor.planFile ? {
                    fileId: floor.planFile.id,
                    temporary: false,
                } : undefined,
                siteId: floor.siteId,
            })
            this.form.useSnackbar(snackBar)
            this.title = `Stockwerk ${this.form.data.name} bearbeiten`
        })

        this.routeSub = route.params.subscribe(async (params) => {
            this.floor.source(() => api.apiFloorIdGet(params["id"])).refresh()
            this.bookableEntities.source(() => api.apiFloorFloorIdBookableEntityGet(params["id"])).refresh()
        })
    }

    public get selectedEntity(): BookableEntityDto | undefined {
        if (this.editingBookableEntity?.id !== undefined) {
            return this.bookableEntities.data?.find((entity) => entity.id === this.editingBookableEntity?.id)
        }

        return undefined
    }

    public set selectedEntity(entity: BookableEntityDto | undefined) {
        this.editingBookableEntity = { ...entity }
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
        this.snackBar.open("Stockwerk erfolgreich aktualisiert", "OK", { duration: 1000 })
        this.floor.refresh()
    }

    ngOnDestroy(): void {
        this.routeSub.unsubscribe()
    }

    public onEditBookableEntity(bookableEntity?: BookableEntityDto) {
        this.editingBookableEntity = { ...bookableEntity}
    }

    public onCreateBookableEntity() {
        this.editingBookableEntity = {
            name: "",
            type: undefined,
            posX: 0,
            posY: 0,
        }
    }

    public onSubmitBookableEntity() {
        if (this.editingBookableEntity === undefined) {
            return
        }

        this.bookableEntities.loading(true)

        const form = new Form(this.editingBookableEntity)
        form.useSnackbar(this.snackBar)

        if (this.editingBookableEntity.id === undefined) {
            form.submit((data) => this.api.apiBookableEntityPost({
                ...data,
                floorId: this.floor.data?.id,
            })).then(() => {
                this.bookableEntities.refresh()
                this.editingBookableEntity = undefined
                this.snackBar.open("Erfolgreich erstellt", "OK", { duration: 1000 })
            })

            return
        }

        if (this.editingBookableEntity.id !== undefined) {
            form.submit((data) => this.api.apiBookableEntityPut({
                ...data,
                floorId: this.floor.data?.id,
            })).then(() => {
                this.bookableEntities.refresh()
                this.editingBookableEntity = undefined
                this.snackBar.open("Erfolgreich aktualisiert", "OK", { duration: 1000 })
            })
        }
    }

    public onCancelEditingBookableEntity() {
        this.editingBookableEntity = undefined
    }

    public onDragBookableEntity(event: { entity: BookableEntityDto, deltaX: number, deltaY: number}) {
        if (this.editingBookableEntity === undefined) {
            return
        }

        this.editingBookableEntity.posX = (this.editingBookableEntity.posX ?? 0) + event.deltaX
        this.editingBookableEntity.posY = (this.editingBookableEntity.posY ?? 0) + event.deltaY
    }

    public get initialFiles(): string[] {
        const planFile = this.form?.data.planFile

        if (planFile === undefined || planFile.temporary === true) {
            return []
        }

        const fileId = planFile.fileId

        if (fileId === undefined) {
            return []
        }

        return [fileId]
    }

    public onFilesUploaded(files: FileUploadDto[]) {
        this.files = files
    }

    public onSelectedEntityPosXUpdate(posX: number) {
        if (this.editingBookableEntity === undefined) {
            return
        }

        this.editingBookableEntity = { ...this.editingBookableEntity, posX }
    }

    public onSelectedEntityPosYUpdate(posY: number) {
        if (this.editingBookableEntity === undefined) {
            return
        }

        this.editingBookableEntity = { ...this.editingBookableEntity, posY }
    }

    public onDeleteEntity(id: string) {
        this.api.apiBookableEntityIdDelete(id).subscribe({
            next: () => {
                if (this.editingBookableEntity?.id === id) {
                    this.editingBookableEntity = undefined
                }
                this.bookableEntities.refresh()
                this.snackBar.open("Erfolgreich gelöscht", "OK", { duration: 1000 })
            },
            error: (error) => {
                this.snackBar.open(error.message, "OK")
            },
        })
    }
}
