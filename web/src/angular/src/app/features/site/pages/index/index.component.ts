import { Component } from "@angular/core"
import { MatDialog } from "@angular/material/dialog"
import { MatSnackBar } from "@angular/material/snack-bar"

import { DefaultService, SiteDto } from "src/app/core/openapi"
import { DataLoader } from "src/app/util"
import { DeleteSiteDialogComponent } from "../../components/delete-site-dialog/delete-site-dialog.component"


@Component({
    selector: "index",
    templateUrl: "./index.component.html",
    styles: [],
})
export class IndexComponent {
    public sites: DataLoader<SiteDto[]> = new DataLoader()

    public dataColumns = [
        { key: "id", label: "ID" },
        { key: "name", label: "Name" },
    ]

    public constructor(
        private readonly api: DefaultService, 
        readonly dialog: MatDialog,
        readonly snackBar: MatSnackBar
    ) {
        this.sites.source( () => api.apiSiteGet() )
        this.sites.refresh()
    }

    public getEditRoute(site: SiteDto) {
        return `./${site.id}/edit`
    }

    public onDelete(site: SiteDto) {     
           
        this.dialog.open(DeleteSiteDialogComponent, {
            data: {
                id: site.id,
                name: site.name,
            },
        }).afterClosed().subscribe(result => {
            if (result === true && site.id !== undefined) {
                this.onDeleteSite(site.id)
            }
        })
    }
    public onDeleteSite(id: string) {
        this.api.apiSiteIdDelete(id).subscribe({
            next: () => {
                this.sites?.refresh()
                this.snackBar.open("Erfolgreich gelöscht", "OK", { duration: 1000 })
            },
            error: (error) => {
                this.snackBar.open(error.message, "OK")
            },
        })
    }
}
