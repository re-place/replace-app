import { Component } from "@angular/core"
import { Router } from "@angular/router"

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

    constructor(private readonly api: ApiService, private readonly router: Router) {}

    public submit() {
        this.form.submit(this.api.createOffice)
    }
}
