import {Component, OnInit} from "@angular/core"
import {MatSnackBar} from "@angular/material/snack-bar"
import { firstValueFrom } from "rxjs"

import { Entity, EntityStatus } from "../../components/entity-map/entity-map.component"
import { Interval } from "../../components/time-selector/time-selector.component"
import { BookingDto, DefaultService, FloorDto, SiteDto } from "src/app/core/openapi"
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

    private _storedSiteId = useLocalStorage<string>("selectedSite")
    private _storedFloorId = useLocalStorage<string>("selectedFloor")

    private _selectedFloor: FloorDto | undefined
    private _selectedSite: SiteDto | undefined

    private _interval = {
        start: new Date(),
        end: new Date(),
    }

    protected selecting: "site" | "floor" | "time" | undefined

    bookableEntities: Entity[] = []
    bookableEntitiesMap: Map<string, Entity> = new Map()

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

            return this.apiService.apiBookingByParamsGet(start, end, undefined, floorId, true)
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
        ]).then(([sites, floors]) => {
            this.sites = sites ?? []
            this.floors = floors ?? []

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
                this.bookableEntities = response.map((entity): Entity => ({
                    status: EntityStatus.AVAILABLE,
                    entity,
                }))

                this.bookableEntitiesMap = new Map(this.bookableEntities.map(entity => [entity.entity.id ?? "", entity]))
                this.bookings.refresh()
            },
            error: () => {
                this.showErrorSnackbar("Buchbare Objekte konnten nicht abgefragt werden")
            },
        })
    }

    protected getBookedEntities(bookings: BookingDto[], bookableEntities: Entity[]): Set<string> {
        if (bookings === undefined || bookings.length === 0) {
            return new Set()
        }

        const bookedEntities =
            new Set(bookings.flatMap((booking) => booking?.bookedEntities?.map((entity) => entity.id)) as string[])

        let uncheckedBookableEntities = bookableEntities.filter((entity) => !bookedEntities.has(entity.entity.id ?? ""))

        let oldSize = bookedEntities.size
        let newSize = bookedEntities.size

        do {
            oldSize = newSize

            for (const entity of uncheckedBookableEntities) {
                const parentId = entity.entity.parentId
                if (parentId === undefined) {
                    continue
                }

                if (!bookedEntities.has(parentId)) {
                    continue
                }

                bookedEntities.add(entity.entity.id ?? "")
            }

            uncheckedBookableEntities
                = uncheckedBookableEntities.filter((entity) => !bookedEntities.has(entity.entity.id ?? ""))

            newSize = bookedEntities.size

        } while (oldSize !== newSize)

        return bookedEntities
    }

    protected getUnavailableEntities(bookedEntitieIds: Set<string>, bookableEntities: Entity[]): Set<string> {
        const bookedEntities = bookableEntities.filter((entity) => bookedEntitieIds.has(entity.entity.id ?? ""))

        const parentIdsOfBookedEntities = new Set<string>(
            bookedEntities.flatMap((entity) => entity.entity.parentId ?? []),
        )

        return new Set(
            bookableEntities
                .filter((entity) => parentIdsOfBookedEntities.has(entity.entity.id ?? ""))
                .map((entity) => entity.entity.id as string),
        )
    }

    updateAvailability(entities: Entity[], bookings: BookingDto[]) {
        const bookedEntities = this.getBookedEntities(bookings, entities)
        const unavailableEntities = this.getUnavailableEntities(bookedEntities, entities)

        this.bookableEntities = entities.map((entity) => {
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
        const selectedEntitiesCount = this.bookableEntities.filter(entity => entity.status === EntityStatus.SELECTED).length

        return this._selectedFloor === undefined || selectedEntitiesCount === 0

    }

    onSubmit() {
        const floorId = this._selectedFloor?.id
        const start = this._interval.start.toISOString()
        const end = this._interval.end.toISOString()

        const entities = this.bookableEntities
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
