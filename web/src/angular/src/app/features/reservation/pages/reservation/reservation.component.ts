import {NGX_MAT_DATE_FORMATS, NgxMatDateFormats} from "@angular-material-components/datetime-picker"
import {Component, OnInit} from "@angular/core"
import {FormControl, FormGroup} from "@angular/forms"
import {MatSnackBar} from "@angular/material/snack-bar"

import {BookableEntityDto, BookingDto, DefaultService, FloorDto, SiteDto} from "src/app/core/openapi"

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
    selectedEntity?: BookableEntityDto

    minDate = new Date()
    timeFormControl = new FormGroup({
        startDate: new FormControl(new Date()),
        endDate: new FormControl(new Date()),
    })

    images = [
        { name: "Darmstadt", path: "assets/andrena_Darmstadt.png" },
        { name: "Frankfurt am Main", path: "assets/andrena_Frankfurt.png" },
    ]

    imgSrc: string = this.images[0].path
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

            if (startDate > endDate) {
                endDate.setFullYear(startDate.getFullYear(), startDate.getMonth(), startDate.getDate())

                endDateControl.reset()
                endDateControl.setValue(endDate)
            }

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

            if (startDate > endDate) {
                startDate.setFullYear(endDate.getFullYear(), endDate.getMonth(), endDate.getDate())

                startDateControl.reset()
                startDateControl.setValue(startDate)
            }

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

    getFloors() {
        this.selectedFloor = undefined
        this.selectedEntity = undefined
        if(this.selectedSite?.id == undefined) return

        this.apiService.apiSiteSiteIdFloorGet(this.selectedSite.id).subscribe({
            next: response => {
                this.floors = response
                this.setImgSrc()
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
        this.selectedEntity = undefined
        if(this.selectedFloor?.id == undefined) return

        this.apiService.apiFloorFloorIdBookableEntityGet(this.selectedFloor.id).subscribe({
            next: response => {
                this.bookableEntities = response
                this.alphaSort(this.bookableEntities)
            },
            error: () => {
                this.showErrorSnackbar("Buchbare Objekte konnten nicht abgefragt werden")
            },
        })
    }

    sendBooking() {
        if(this.selectedEntity?.id === undefined) {
            this.showErrorSnackbar("Kein Arbeitsplatz ausgewählt")
            return
        }
        const bookingDto: BookingDto = {
            bookedEntities: [
                this.selectedEntity.id,
            ],
            startDateTime: this.timeFormControl.value.startDate?.toISOString(),
            endDateTime: this.timeFormControl.value.endDate?.toISOString(),
        }
        this.apiService.apiBookingPost(bookingDto).subscribe({
            next: () => {
                this.snackBar.open("Arbeitsplatz gebucht!", "ok", { duration: 3000 })
            },
            error: error => {
                switch(error.status) {
                case 400:
                    this.showErrorSnackbar("Arbeitsplatz ist nicht verfügbar")
                    break
                default:
                    break
                    this.showErrorSnackbar("Arbeitsplatz konnte nicht gebucht werden")
                }
            },
        })
    }

    setImgSrc() {
        if(this.selectedSite?.name == undefined) return
        const site = this.selectedSite??{}
        this.imgSrc = this.images.find(img => img.name == site.name)?.path??""
        console.log(this.imgSrc)
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
