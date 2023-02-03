import {Component, OnInit} from "@angular/core"
import {FormControl, FormGroup} from "@angular/forms"
import {MatSnackBar} from "@angular/material/snack-bar"
import {NGX_MAT_DATE_FORMATS, NgxMatDateFormats} from "@angular-material-components/datetime-picker"

import {BookableEntityDto, BookingDto, CreateBookingDto, DefaultService, FloorDto, SiteDto} from "src/app/core/openapi"


const INTL_DATE_INPUT_FORMAT = {
    year: "numeric",
    month: "numeric",
    day: "numeric",
    hourCycle: "h23",
    hour: "2-digit",
    minute: "2-digit",
}

const MAT_DATE_FORMATS: NgxMatDateFormats = {
    parse: {
        dateInput: INTL_DATE_INPUT_FORMAT,
    },
    display: {
        dateInput: INTL_DATE_INPUT_FORMAT,
        monthYearLabel: { year: "numeric", month: "short" },
        dateA11yLabel: { year: "numeric", month: "long", day: "numeric" },
        monthYearA11yLabel: { year: "numeric", month: "long" },
    },
}
@Component({
    selector: "reservation",
    templateUrl: "./reservation.component.html",
    styles: [],
    providers: [{ provide: NGX_MAT_DATE_FORMATS, useValue: MAT_DATE_FORMATS }],
})
export class ReservationComponent implements OnInit {
    sites: SiteDto[] = []
    selectedSite?: SiteDto

    floors: FloorDto[] = []
    selectedFloor?: FloorDto

    bookableEntities: BookableEntityDto[] = []
    selectedEntities: BookableEntityDto[] = []

    allBookings: BookingDto[] = []
    bookings: BookingDto[] = []

    minDate = new Date()
    timeFormControl = new FormGroup({
        startDate: new FormControl(new Date()),
        endDate: new FormControl(new Date()),
    })

    constructor(
        private readonly apiService: DefaultService,
        private readonly snackBar: MatSnackBar,
    ) {

        this.timeFormControl.get("startDate")?.valueChanges.subscribe((startDate) => {
            if (startDate === null) {
                return
            }

            const endDateControl = this.timeFormControl.get("endDate")

            if (endDateControl === null || endDateControl === undefined) {
                return
            }

            const endDate = endDateControl.value

            if (endDate === null || endDate === undefined) {
                return
            }

            if (
                startDate > endDate
            ) {
                endDate.setFullYear(startDate.getFullYear(), startDate.getMonth(), startDate.getDate())

                endDateControl.reset()
                endDateControl.setValue(endDate)
            }
            this.getBookings()

        })

        this.timeFormControl.get("endDate")?.valueChanges.subscribe((endDate) => {
            if (endDate === null) {
                return
            }

            const startDateControl = this.timeFormControl.get("startDate")

            if (startDateControl === null || startDateControl === undefined) {
                return
            }

            const startDate = startDateControl.value

            if (startDate === null || startDate === undefined) {
                return
            }

            if (
                startDate > endDate
            ) {
                startDate.setFullYear(endDate.getFullYear(), endDate.getMonth(), endDate.getDate())

                startDateControl.reset()
                startDateControl.setValue(startDate)
            }
            this.getBookings()

        })
    }

    ngOnInit() {
        this.apiService.apiSiteGet().subscribe({
            next: response => {
                this.sites = response
                this.alphaSort(this.sites)
                this.setDefaults()
            },
            error: () => {
                this.showErrorSnackbar("Standort konnten nicht abgefragt werden")
            },
        })
    }

    // Set default to Darmstadt for the first version

    setDefaults() {
        this.selectedSite = this.sites.find(site => site.name == "Darmstadt")
        this.getFloors()

        const startDate = new Date(new Date().setHours(0,0,0))
        const endDate = new Date(new Date().setHours(23,59,0))

        this.timeFormControl.reset()
        this.timeFormControl.get("startDate")?.setValue(startDate)
        this.timeFormControl.get("endDate")?.setValue(endDate)
    }

    getBookings() {
        const start = this.timeFormControl.get("startDate")?.value?.toISOString()??new Date().toISOString()
        const end = this.timeFormControl.get("endDate")?.value?.toISOString()??new Date().toISOString()

        this.apiService.apiBookingByParamsGet(start, end).subscribe({
            next: result => {
                this.bookings = result
            },
            error: err => {
                console.log(err)
                this.showErrorSnackbar("Buchungen konnten nicht abgefragt werden")
            },
        })
    }

    getFloors() {
        this.selectedFloor = undefined
        this.selectedEntities = []
        if(this.selectedSite?.id == undefined) return

        this.apiService.apiSiteSiteIdFloorGet(this.selectedSite.id).subscribe({
            next: response => {
                this.floors = response
                if(this.floors.length == 1) {
                    this.selectedFloor = this.floors[0]
                    this.getBookableEntities()
                    return
                }
                this.alphaSort(this.floors)
            },
            error: () => {
                this.showErrorSnackbar("Stockwerke für den Standort konnten nicht abgefragt werden")
            },
        })

    }

    getBookableEntities() {
        this.selectedEntities = []
        const selectedFloorId = this.selectedFloor?.id
        if(selectedFloorId === undefined) return

        this.apiService.apiFloorFloorIdBookableEntityGet(selectedFloorId).subscribe({
            next: response => {
                this.bookableEntities = response
                this.alphaSort(this.bookableEntities)
            },
            error: () => {
                this.showErrorSnackbar("Buchbare Objekte konnten nicht abgefragt werden")
            },
        })
    }

    updateSelectedEntities(entity: BookableEntityDto, checked: boolean) {
        if(checked) {
            this.selectedEntities.push(entity)
        } else {
            this.selectedEntities = this.selectedEntities.filter(ent => ent != entity)
        }
    }

    sendBooking() {
        const ids: string[] = this.selectedEntities.map(entity => entity.id??"")
        if(ids == undefined || ids.length < 1) {
            this.showErrorSnackbar("Kein Arbeitsplatz ausgewählt")
            return
        }

        const bookingDto: CreateBookingDto = {
            start: this.timeFormControl.value.startDate?.toISOString(),
            end: this.timeFormControl.value.endDate?.toISOString(),
            bookedEntityIds: ids,
        }
        this.apiService.apiBookingPost(bookingDto).subscribe({
            next: () => {
                this.snackBar.open("Arbeitsplatz gebucht!", "ok", { duration: 3000 })
                this.selectedSite = undefined
                this.selectedFloor = undefined
                this.selectedEntities = []
                this.getBookings()
            },
            error: error => {
                switch(error.status) {
                case 400:
                    this.showErrorSnackbar("Arbeitsplatz ist nicht verfügbar")
                    break
                default:
                    this.showErrorSnackbar("Arbeitsplatz konnte nicht gebucht werden")
                    break
                }
            },
        })
    }

    isBooked(id?: string) {
        if(id === undefined) return true
        return this.bookings.some(booking => booking.bookedEntities?.some(dto => dto.id === id))
    }

    get imgSrc() {
        const planFileUrl = this.selectedFloor?.planFile?.url

        return planFileUrl ?? ""
    }

    alphaSort(arr: any[]) {
        arr.sort((a,b) => {
            if(a?.name == undefined || b?.name == undefined) return -1
            return a.name > b.name ? 1 : -1
        })
    }

    showErrorSnackbar(message: string) {
        this.snackBar.open(message, "error", { duration: 3000 })
    }

    dateFilter: (date: Date | null) => boolean =
        (date: Date | null) => {
            return !(!date || date.getDay() == 0 || date.getDay() == 6)
        }
}
