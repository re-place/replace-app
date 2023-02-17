import {Component, OnInit} from "@angular/core"
import {MatSnackBar} from "@angular/material/snack-bar"

import { Entity } from "../../components/entity-map/entity-map.component"
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

    private _storedSiteId = useLocalStorage<string>("selectedSite")
    private _storedFloorId = useLocalStorage<string>("selectedFloor")

    private _selectingSite = false
    private _selectingFloor = false
    private _selectingTime = false

    private _start = new Date()
    private _end = new Date()

    bookableEntities: Entity[] = []

    bookedEntities: Set<string> = new Set()

    public bookings: DataLoader<BookingDto[]>   = new DataLoader(
        () => {
            const floorId = this.selectedFloor?.id
            const start = this._start.toISOString()
            const end = this._end.toISOString()

            if (floorId === undefined) {
                return Promise.resolve([])
            }

            return this.apiService.apiBookingByParamsGet(start, end, undefined, floorId, true)
        },
    )

    constructor(
        private readonly apiService: DefaultService,
        private readonly snackBar: MatSnackBar,
    ) {
        this.bookings.subscribe((data) => {
            this.bookedEntities
                = new Set(data.flatMap((booking) => booking?.bookedEntities?.map((entity) => entity.id)) as string[])

            this.updateAvailability()
        })
    }

    ngOnInit() {
        this.apiService.apiSiteGet().subscribe({
            next: response => {
                this.sites = response
            },
            error: () => {
                this.showErrorSnackbar("Standort konnten nicht abgefragt werden")
            },
        })

        this.apiService.apiFloorGet().subscribe({
            next: response => {
                this.floors = response
                this.refreshBookableEntities()
                this.bookings.refresh()
            },
            error: () => {
                this.showErrorSnackbar("Stockwerke konnten nicht abgefragt werden")
            },
        })

        this._start.setHours(8, 0, 0, 0)
        this._start.setDate(this._start.getDate() + 1)
        this._end.setHours(18, 0, 0, 0)
        this._end.setDate(this._end.getDate() + 1)
    }


    showErrorSnackbar(message: string) {
        this.snackBar.open(message, "error", { duration: 3000 })
    }

    refreshBookableEntities() {
        if (this.selectedFloor === undefined) {
            return
        }

        this.apiService.apiFloorFloorIdBookableEntityGet(this.selectedFloor.id ?? "").subscribe({
            next: response => {
                this.bookableEntities = response.map((entity): Entity => ({
                    available: true,
                    selected: false,
                    entity,
                }))
            },
            error: () => {
                this.showErrorSnackbar("Buchbare Objekte konnten nicht abgefragt werden")
            },
        })
    }

    updateAvailability() {
        const entities = this.bookableEntities

        for (const entity of entities) {
            entity.available = !this.bookedEntities.has(entity.entity.id ?? "")
        }

        this.bookableEntities = [...entities]
    }

    get selectedSite() {
        if (this._storedSiteId.value === undefined) return undefined

        return this.sites.find(site => site.id === this._storedSiteId.value)
    }

    set selectedSite(site: SiteDto | undefined) {
        if (site?.id !== this._storedSiteId.value) {
            this._storedFloorId.value = undefined
        }

        this._storedSiteId.value = site?.id
        this._selectingSite = false
    }

    get selectedFloor() {
        if (this._storedFloorId.value === undefined) return undefined

        return this.selectableFloors.find(floor => floor.id === this._storedFloorId.value)
    }

    set selectedFloor(floor: FloorDto | undefined) {
        this._storedFloorId.value = floor?.id
        this._selectingFloor = false

        this.refreshBookableEntities()
        this.bookings.refresh()
    }

    get siteTagClasses() {
        if (this.selectedSite === undefined) {
            return ""
        }

        return "hover:text-gray-400 cursor-pointer"
    }

    get floorTagClasses() {
        if (this.selectedFloor === undefined) {
            return ""
        }

        return "hover:text-gray-400 cursor-pointer"
    }

    get isSelectingSite() {
        return this.selectedSite === undefined || this._selectingSite
    }

    set isSelectingSite(value: boolean) {
        this._selectingSite = value
        if (value) {
            this.isSelectingFloor = false
        }
    }

    get isSelectingTime() {
        return this._selectingTime
    }

    set isSelectingTime(value: boolean) {
        this._selectingTime = value
    }

    get isSelectingFloor() {
        return this.selectedFloor === undefined || this._selectingFloor
    }

    set isSelectingFloor(value: boolean) {
        this._selectingFloor = value

        if (value) {
            this.isSelectingSite = false
        }
    }

    get selectableFloors() {
        const site = this.selectedSite

        if (site === undefined) {
            return []
        }

        return this.floors.filter(floor => floor.siteId === site.id)
    }

    get interval() {
        return {
            start: this._start,
            end: this._end,
        }
    }

    set interval(interval: Interval) {
        this._start = interval.start
        this._end = interval.end
        this.bookings.refresh()
    }

    get disabled() {
        return this.selectedFloor === undefined || this.bookableEntities.filter(entity => entity.selected).length === 0
    }

    onSubmit() {
        const floorId = this.selectedFloor?.id
        const start = this._start.toISOString()
        const end = this._end.toISOString()
        const entities = this.bookableEntities.filter(entity => entity.selected).map(entity => entity.entity.id as string)

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
            },
            error: () => {
                this.showErrorSnackbar("Buchung konnte nicht erstellt werden")
            },
        })
    }
}
