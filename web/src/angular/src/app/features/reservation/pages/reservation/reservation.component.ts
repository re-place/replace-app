import { Component, OnInit } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"

import { BookableEntityDto, DefaultService, FloorDto, SiteDto } from "src/app/core/openapi"

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

    constructor(
        private readonly apiService: DefaultService,
        private readonly snackBar: MatSnackBar,
    ) { }

    ngOnInit() {
        this.apiService.apiSiteGet().subscribe({
            next: response => {
                this.sites = response
                this.alphaSort(this.sites)
            },
            error: () => {
                this.showErrorSnackbar("Standort konnten nicht abgefragt werden")
            },
        })
    }

    // TODO:
    // If floors.length == 1
    //  pick the only element
    //  mark corresponding field as read only
    //  be sure to call getBookEnts manually
    getFloors() {
        if(this.selectedSite?.id == undefined) return

        this.apiService.apiSiteSiteIdFloorGet(this.selectedSite.id).subscribe({
            next: response => {
                this.floors = response
                this.alphaSort(this.floors)
            },
            error: () => {
                this.showErrorSnackbar("Stockwerke für den Standort konnten nicht abgefragt werden")
            },
        })

    } 

    getBookableEntities() {
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

    alphaSort(arr: any[]) {
        arr.sort((a,b) => {
            if(a?.name == undefined || b?.name == undefined) return -1
            return a.name > b.name ? 1 : -1
        })
    }

    showErrorSnackbar(message: string) {
        this.snackBar.open(message, "error", { duration: 3000 })
    }

}
