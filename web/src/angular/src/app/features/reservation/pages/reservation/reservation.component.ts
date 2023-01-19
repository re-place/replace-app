import {Component, OnInit} from "@angular/core"
import {FormControl, FormGroup} from "@angular/forms"
import {MatSnackBar} from "@angular/material/snack-bar"

import {BookableEntityDto, BookingDto, DefaultService, FloorDto, SiteDto} from "src/app/core/openapi"

@Component({
    selector: "reservation",
    templateUrl: "./reservation.component.html",
    styles: [],
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

            if (
                startDate > endDate
            ) {
                endDate.setFullYear(startDate.getFullYear(), startDate.getMonth(), startDate.getDate())

                endDateControl.reset()
                endDateControl.setValue(endDate)
            }
            this.getCurrentBookings()

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
            this.getCurrentBookings()

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
        this.getBookings()
    }
    // Set default to Darmstadt for the first version

    setDefaults() {
        this.selectedSite = this.sites.find(site => site.name == "Darmstadt")
        this.getFloors()

        const startDate = new Date()
        startDate.setHours(startDate.getHours() + 1)

        const oldStartDate = this.timeFormControl.value.startDate
        oldStartDate?.setHours(startDate.getHours())

        const endDate = new Date()
        endDate.setHours(startDate.getHours() + 1)

        const oldEndDate = this.timeFormControl.value.endDate
        oldEndDate?.setHours(endDate.getHours())

        this.timeFormControl.get("startDate")?.setValue(startDate)
        this.timeFormControl.get("endDate")?.setValue(endDate)
    }

    getCurrentBookings() {
        const start = this.timeFormControl.get("startDate")?.value?.getTime()??0
        const end = this.timeFormControl.get("endDate")?.value?.getTime()??0

        this.bookings = this.allBookings.filter(i => {
            if(i.startDateTime == undefined || i.endDateTime == undefined) return false
                const startTime = new Date(i.startDateTime).getTime()
            const endTime = new Date(i.endDateTime).getTime()
            return !(startTime > end || endTime < start)
        })
    }

    getBookings() {
        this.apiService.apiBookingGet().subscribe({
            next: response => {
                this.allBookings = response
                this.getCurrentBookings()
            },
            error: () => {
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
        this.selectedEntities = []
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

        const bookingDto: BookingDto = {
            startDateTime: this.timeFormControl.value.startDate?.toISOString(),
            endDateTime: this.timeFormControl.value.endDate?.toISOString(),
            bookedEntities: ids,
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
        if(!id) return true
        return this.bookings.some(booking => booking.bookedEntities?.some(eid => eid == id))
    }

    setImgSrc() {
        if(this.selectedSite?.name == undefined) return
        const site = this.selectedSite??{}
        this.imgSrc = this.images.find(img => img.name == site.name)?.path??""
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
