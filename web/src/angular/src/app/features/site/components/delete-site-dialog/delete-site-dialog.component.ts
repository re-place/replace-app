import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DefaultService, SiteDto } from 'src/app/core/openapi';
import { DataLoader } from 'src/app/util';

@Component({
  selector: 'delete-site-dialog',
  templateUrl: './delete-site-dialog.component.html',
  styles: [
  ]
})
export class DeleteSiteDialogComponent {
  public site = new DataLoader<SiteDto[]>()

  constructor (
    @Inject(MAT_DIALOG_DATA) public data: {id:string, name:string },
    private readonly api: DefaultService
    ) {}
  // ngOnInit(): void {
  //   const now = new Date()
  //   this.site.source(() => this.api.apiSiteGet(undefined, false, undefined)).refresh()
  // }


}
