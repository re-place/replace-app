import {Component, OnInit} from "@angular/core"
import {FormControl} from "@angular/forms"
import {MatDatepickerInputEvent} from "@angular/material/datepicker"
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
    selectedEntity?: BookableEntityDto

    startDate = new Date()
    endDate = new Date()
    startTime = new Date()
    endTime = new Date()

    earliestBookableDate = new Date()
    toggle = false


    images = [
        { name: "Darmstadt", path: "assets/andrena_Darmstadt.png" },
        { name: "Frankfurt am Main", path: "assets/andrena_Frankfurt.png" },
    ]

    imgSrc: string = this.images[0].path

    constructor(
        private readonly apiService: DefaultService,
        private readonly snackBar: MatSnackBar,
    ) { }
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
        this.endTime.setHours(this.endTime.getUTCHours()+1)
    }

    getTime(event: any) {
        if (event.value == undefined) {
            return
        }
        const time = event?.target?.value
        const [hour, minutes] = time.split(":")
        if (event.targetElement.id == "mat-input-1") {
            this.startTime.setHours(hour, minutes)
        }
        else if (event.targetElement.id == "mat-input-3") {
            this.endTime.setHours(hour, minutes)
        }
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
        if(this.selectedEntity?.id == undefined) {
            this.showErrorSnackbar("Kein Arbeitsplatz ausgewählt")
            return
        }
        this.startDate.setHours(this.startTime.getHours(), this.startTime.getMinutes())
        if (!this.toggle) {
            this.endDate.setHours(this.endTime.getHours(), this.endTime.getMinutes())
        } else {
            this.endDate.setTime(this.startDate.getTime())
            this.endDate.setHours(this.endTime.getHours(), this.endTime.getMinutes())
        }
        const bookingDto: BookingDto = {
            bookedEntities: [
                this.selectedEntity.id,
            ],
            startDateTime: this.startDate.toISOString(),
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

    toggleRange() {
        this.toggle = !this.toggle
    }

    dateFilter: (date: Date | null) => boolean =
        (date: Date | null) => {
            return !(!date || date.getDay() == 0 || date.getDay() == 6)
        }
    dateControl= new FormControl(this.startDate)

    saveDate($event: MatDatepickerInputEvent<Date>) {
        if ($event.value == null) {
            return
        }
        else if ($event.targetElement.id == "mat-input-0") {
            this.startDate.setTime($event.value.getTime())
            this.startDate.setSeconds(0,0)
        }
        else if (this.toggle && $event.targetElement.id == "mat-input-2") {
            this.endDate.setTime($event.value.getTime())
            this.endDate.setSeconds(0,0)
        }
    }

    saveStartTime(date: Event) {
        console.log((<Date><unknown>date).toString())
    }
}
