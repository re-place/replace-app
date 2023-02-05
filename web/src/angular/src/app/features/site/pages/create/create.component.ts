import { Component } from "@angular/core"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ActivatedRoute, Router } from "@angular/router"

import { CreateSiteDto, DefaultService } from "src/app/core/openapi"
import { Form } from "src/app/util"

@Component({
    selector: "create",
    templateUrl: "./create.component.html",
    styles: [],
})
export class CreateComponent {
    public form = new Form<CreateSiteDto>({
        name: "",
    })

    constructor(
        private readonly api: DefaultService,
        public readonly router: Router,
        public readonly route: ActivatedRoute,
        private readonly snackBar: MatSnackBar,
    ) {
        this.form.useSnackbar(snackBar)
    }

    public submit() {
        this.form.submit((data) => this.api.apiSitePost(data), {
            onSuccess: (site) => {
                this.router.navigate(["./../", site.id, "edit"], { relativeTo: this.route })
            },
        })
    }
}
