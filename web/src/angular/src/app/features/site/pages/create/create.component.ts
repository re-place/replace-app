import { Component } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute, Router } from "@angular/router"

import { ApiService } from "src/app/core/services/api.service"
import { Form } from "src/app/util"

@Component({
    selector: "create",
    templateUrl: "./create.component.html",
    styles: [],
})
export class CreateComponent {
    public form = new Form({
        name: "",
    })

    constructor(
        private readonly api: ApiService,
        public readonly router: Router,
        public readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.form.useSnackbar(snackBar)
    }

    public async submit() {
        const site = await this.form.submit((data) => this.api.createSite(data))

        if (site === undefined) {
            return
        }

        this.router.navigate(["./../", site.id, "edit"], { relativeTo: this.route })
    }
}
