import { Component, Inject } from "@angular/core"
import { MAT_DIALOG_DATA } from "@angular/material/dialog"
import { DefaultService, BookableEntityDto, BookingDto, SiteDto } from "src/app/core/openapi"
import { DataLoader } from "src/app/util"
import { toLocaleDateTimeString } from "src/app/util/DateTime"
@Component({
  selector: 'delete-floor-dialog',
  templateUrl: './delete-floor-dialog.component.html',
  styles: [
  ]
})
export class DeleteFloorDialogComponent {
  constructor (
    @Inject(MAT_DIALOG_DATA) public data: {id:string, name:string },
    private readonly api: DefaultService
    ) {}
}
