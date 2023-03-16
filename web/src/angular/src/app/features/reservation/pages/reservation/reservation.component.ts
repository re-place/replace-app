import { Component, OnInit} from "@angular/core"
import {MatSnackBar} from "@angular/material/snack-bar"
import { firstValueFrom } from "rxjs"

import { Entity, EntityStatus, interactableStates } from "../../components/entity-map/entity-map.component"
import { Interval } from "../../components/time-selector/time-selector.component"
import { BookableEntityDto, BookableEntityTypeDto, BookingDto, DefaultService, FloorDto, SiteDto } from "src/app/core/openapi"
import { DataLoader } from "src/app/util"
import { useLocalStorage } from "src/app/util/LocalStorage"

@Component({
    selector: "reservation",
    templateUrl: "./reservation.component.html",
    styles: [],
})
export class ReservationComponent implements OnInit {
    sites: SiteDto[] = []
    floors: FloorDto[] = []
    siteFloors: FloorDto[] = []
    types: BookableEntityTypeDto[] = []

    private _storedSiteId = useLocalStorage<string>("selectedSite")
    private _storedFloorId = useLocalStorage<string>("selectedFloor")
    private _storedTypeId = useLocalStorage<string>("selectedType")

    private _selectedFloor: FloorDto | undefined
    private _selectedSite: SiteDto | undefined
    private _selectedType: BookableEntityTypeDto | undefined

    private _interval = {
        start: new Date(),
        end: new Date(),
    }

    protected selecting: "site" | "floor" | "time" | undefined

    visibleBookableEntities: Entity[] = []
    visibleBookableEntitiesMap: Map<string, Entity> = new Map()
    bookableEntities: BookableEntityDto[] = []

    bookedEntityIds: Set<string> = new Set()
    unavailableEntityIds: Set<string> = new Set()

    public bookings: DataLoader<BookingDto[]>   = new DataLoader(
        () => {
            const floorId = this._selectedFloor?.id
            const start = this._interval.start.toISOString()
            const end = this._interval.end.toISOString()

            if (floorId === undefined) {
                return Promise.resolve([])
            }

            return this.apiService.apiBookingByParamsGet(start, end, undefined, floorId, false)
        },
    )

    public mobileDateTimeFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

    constructor(
        private readonly apiService: DefaultService,
        private readonly snackBar: MatSnackBar,
    ) {
        this.apiService.apiBookableEntityTypeGet().subscribe({
            next: result => {
                this.types = result
            },
        })
        this.bookings.subscribe((data) => {
            this.updateAvailability(this.bookableEntities, data)
        })
    }

    ngOnInit() {
        Promise.all([
            firstValueFrom(this.apiService.apiSiteGet())
                .catch(() => this.showErrorSnackbar("Standort konnte nicht geladen werden")),
            firstValueFrom(this.apiService.apiFloorGet())
                .catch(() => this.showErrorSnackbar("Stockwerke konnten nicht geladen werden")),
            firstValueFrom(this.apiService.apiBookableEntityTypeGet())
                .catch(() => this.showErrorSnackbar("Buchbare Entitäten konnten nicht geladen werden")),
        ]).then(([sites, floors, types]) => {
            this.sites = sites ?? []
            this.floors = floors ?? []
            this.types = types ?? []

            if (this.sites.length === 0) {
                this.showErrorSnackbar("Keine Standorte gefunden")
                return
            }

            if (this.floors.length === 0) {
                this.showErrorSnackbar("Keine Stockwerke gefunden")
                return
            }

            if (this._storedSiteId.value !== undefined) {
                this.setSelectedSite(this.sites.find(s => s.id === this._storedSiteId.value))
            }

            if (this._storedFloorId.value !== undefined) {
                this.setSelectedFloor(this.siteFloors.find(f => f.id === this._storedFloorId.value))
            }

            if (this._storedTypeId.value !== undefined) {
                this.setSelectedType(this.types.find(t => t.id === this._storedTypeId.value)?.id)
            } else {
                this.setSelectedType(this.types.at(0)?.id)
            }
        })

        const start = this._interval.start
        const end = this._interval.end

        start.setHours(8, 0, 0, 0)
        start.setDate(start.getDate() + 1)
        end.setHours(18, 0, 0, 0)
        end.setDate(end.getDate() + 1)
    }


    showErrorSnackbar(message: string) {
        this.snackBar.open(message, "error", { duration: 3000 })
    }

    refreshBookableEntities() {
        if (this._selectedFloor === undefined) {
            return
        }

        this.apiService.apiFloorFloorIdBookableEntityGet(this._selectedFloor.id ?? "").subscribe({
            next: response => {
                this.bookableEntities = response
                this.bookings.refresh()
            },
            error: () => {
                this.showErrorSnackbar("Buchbare Objekte konnten nicht abgefragt werden")
            },
        })
    }

    protected getAncestors(entity: Entity, bookableEntities: Entity[]) {
        const ancestors: Entity[] = []

        let currentEntity = entity
        const checkedEntities = new Set<string>()

        while (currentEntity.entity.parentId !== undefined) {
            const parent = bookableEntities.find(e => e.entity.id === currentEntity.entity.parentId)

            if (parent === undefined || checkedEntities.has(parent.entity.id as string)) {
                break
            }

            checkedEntities.add(parent.entity.id as string)

            ancestors.push(parent)
            currentEntity = parent
        }

        return ancestors
    }

    protected getDescendants(entity: Entity, bookableEntities: Entity[]) {
        const descendants: Entity[] = []

        const queue: Entity[] = [entity]
        const checkedEntities = new Set<string>()

        while (queue.length > 0) {
            const currentEntity = queue.shift() as Entity

            if (checkedEntities.has(currentEntity.entity.id as string)) {
                continue
            }

            checkedEntities.add(currentEntity.entity.id as string)

            const children = bookableEntities.filter(e => e.entity.parentId === currentEntity.entity.id)
            descendants.push(...children)
            queue.push(...children)
        }

        return descendants
    }

    protected getBookedEntities(bookings: BookingDto[], bookableEntities: Entity[]): Set<string> {
        if (bookings === undefined || bookings.length === 0) {
            return new Set()
        }

        const bookedEntityIds =
            new Set(bookings.flatMap((booking) => booking?.bookedEntities?.map((entity) => entity.id)) as string[])

        const bookedEntities = bookableEntities
            .filter((entity) => bookedEntityIds.has(entity.entity.id as string))
            .flatMap((entitiy) => [entitiy, ...this.getDescendants(entitiy, bookableEntities)])

        return new Set(bookedEntities.map((entity) => entity.entity.id as string))
    }

    protected getUnavailableEntities(bookedEntitieIds: Set<string>, bookableEntities: Entity[]): Set<string> {
        const bookedEntities = bookableEntities.filter((entity) => bookedEntitieIds.has(entity.entity.id ?? ""))

        const unavailableEntities = bookedEntities.flatMap((entity) => [...this.getAncestors(entity, bookableEntities)])

        return new Set(unavailableEntities.map((entity) => entity.entity.id as string))
    }

    updateAvailability(bookableEntities: BookableEntityDto[], bookings: BookingDto[]) {
        const entities = bookableEntities.map((entity) => ({
            entity,
            status: EntityStatus.AVAILABLE,
        }))

        const bookedEntities = this.getBookedEntities(bookings, entities)
        const unavailableEntities = this.getUnavailableEntities(bookedEntities, entities)

        this.visibleBookableEntities = entities
            .map((entity) => {
                if (bookedEntities.has(entity.entity.id ?? "")) {
                    entity.status = EntityStatus.BOOKED
                    return entity
                }

                if (unavailableEntities.has(entity.entity.id ?? "")) {
                    entity.status = EntityStatus.DISABLED
                    return entity
                }

                if (entity.status === EntityStatus.SELECTED) {
                    return entity
                }

                entity.status = EntityStatus.AVAILABLE
                return entity
            })
            .filter((entity) => {
                if (this._selectedType === undefined) {
                    return true
                }

                if (entity.entity.typeId === undefined) {
                    return true
                }

                return entity.entity.typeId === this._selectedType.id
            })

        this.triggerBookableEntitiesChanges()
    }

    onBookableEntitySelection(id: string | Entity) {
        id = typeof id === "string" ? id : id.entity.id as string

        const entity = this.visibleBookableEntitiesMap.get(id)

        if (!interactableStates.includes(entity?.status)) {
            return
        }

        if (entity?.status === EntityStatus.SELECTED) {
            entity.status = EntityStatus.AVAILABLE

            const descendants = this.getDescendants(entity, this.visibleBookableEntities)

            for (const descendant of descendants) {
                if (descendant.status === EntityStatus.SELECTED_DISABLED || descendant.status === EntityStatus.SELECTED) {
                    descendant.status = EntityStatus.AVAILABLE
                }
            }
        } else if (entity?.status === EntityStatus.AVAILABLE) {
            entity.status = EntityStatus.SELECTED

            const descendants = this.getDescendants(entity, this.visibleBookableEntities)

            for (const descendant of descendants) {
                if (descendant === entity) {
                    continue
                }

                if (descendant.status === EntityStatus.AVAILABLE || descendant.status === EntityStatus.SELECTED) {
                    descendant.status = EntityStatus.SELECTED_DISABLED
                }
            }
        }

        this.triggerBookableEntitiesChanges()

    }

    protected triggerBookableEntitiesChanges() {
        this.visibleBookableEntities = [...this.visibleBookableEntities]
        this.visibleBookableEntitiesMap = new Map(this.visibleBookableEntities.map(entity => [entity.entity.id ?? "", entity]))
    }

    get selectedSite() {
        return this._selectedSite
    }

    setSelectedSite(site: SiteDto | undefined) {
        this._storedSiteId.value = site?.id
        this.selecting = undefined

        if (site === undefined) {
            this._selectedSite = undefined
            return
        }

        this._selectedSite = this.sites.find(s => s.id === site.id)
        this.siteFloors = this.floors.filter(f => f.siteId === site.id)

        if (this._selectedFloor === undefined) {
            return
        }

        if (this.siteFloors.some((floor) => floor.id === this._selectedFloor?.id)) {
            return
        }

        this.setSelectedFloor(undefined)
    }

    get selectedFloor() {
        return this._selectedFloor
    }

    setSelectedFloor(floor: FloorDto | undefined) {
        this._storedFloorId.value = floor?.id
        this.selecting = undefined

        if (floor === undefined) {
            this._selectedFloor = undefined
        } else {
            this._selectedFloor = this.siteFloors.find(f => f.id === floor.id)
        }


        this.refreshBookableEntities()
    }

    get selectedType() {
        return this._selectedType
    }

    setSelectedType(typeId: string | undefined) {
        this._storedTypeId.value = typeId

        if (typeId === undefined) {
            this._selectedType = undefined
        } else {
            this._selectedType = this.types.find(t => t.id === typeId)
        }

        this.updateAvailability(this.bookableEntities, this.bookings.data ?? [])
    }

    get siteTagClasses() {
        if (this._selectedSite === undefined) {
            return ""
        }

        return "hover:text-gray-400 cursor-pointer"
    }

    get floorTagClasses() {
        if (this._selectedFloor === undefined) {
            return ""
        }

        return "hover:text-gray-400 cursor-pointer"
    }

    get isSelecting(): "site" | "floor" | "time" | undefined {
        if (this._selectedSite === undefined) {
            return "site"
        }

        if (this._selectedFloor === undefined) {
            return "floor"
        }

        return this.selecting
    }

    get isSelectingSite() {
        return this.isSelecting === "site"
    }

    set isSelectingSite(value: boolean) {
        if (value) {
            this.selecting = "site"
            return
        }

        if (!this.isSelectingSite) {
            return
        }

        this.selecting = undefined
    }

    get isSelectingFloor() {
        return this.isSelecting === "floor"
    }

    set isSelectingFloor(value: boolean) {
        if (value) {
            this.selecting = "floor"
            return
        }

        if (!this.isSelectingFloor) {
            return
        }

        this.selecting = undefined
    }

    get isSelectingTime() {
        return this.isSelecting === "time"
    }

    set isSelectingTime(value: boolean) {
        if (value) {
            this.selecting = "time"
            return
        }

        if (!this.isSelectingTime) {
            return
        }

        this.selecting = undefined
    }

    get interval() {
        return this._interval
    }

    setInterval(interval: Interval) {
        this._interval = interval
        this.bookings.refresh()
    }

    get disabled() {
        const selectedEntitiesCount =
            this.visibleBookableEntities.filter(entity => entity.status === EntityStatus.SELECTED).length

        return this._selectedFloor === undefined || selectedEntitiesCount === 0

    }

    onSubmit() {
        const floorId = this._selectedFloor?.id
        const start = this._interval.start.toISOString()
        const end = this._interval.end.toISOString()

        const entities = this.visibleBookableEntities
            .filter(entity => entity.status === EntityStatus.SELECTED)
            .map(entity => entity.entity.id as string)

        if (floorId === undefined) {
            return
        }

        this.apiService.apiBookingPost({
            start,
            end,
            bookedEntityIds: entities,
        }).subscribe({
            next: () => {
                this.bookings.refresh()
                this.snackBar.open("Erfolgreich gebucht", "OK", { duration: 3000 })
            },
            error: () => {
                this.showErrorSnackbar("Buchung konnte nicht erstellt werden")
            },
        })
    }
}
