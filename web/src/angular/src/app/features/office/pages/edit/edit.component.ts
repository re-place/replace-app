import { Component, OnDestroy } from "@angular/core"
import { ActivatedRoute } from "@angular/router"
import { Subscription } from "rxjs"
import { Office } from "types"

import { ApiService } from "src/app/core/services/api.service"
import { Form } from "src/app/util"

@Component({
    selector: "edit",
    templateUrl: "./edit.component.html",
    styles: [],
})
export class EditComponent implements OnDestroy {
    title = ""
    form: Form<Office> | undefined = undefined

    private readonly routeSub: Subscription

    constructor(private readonly api: ApiService, private readonly route: ActivatedRoute) {
        this.routeSub = route.params.subscribe(async (params) => {
            this.form = new Form(await api.getOffice(params["id"]))
            this.title = `Büro ${this.form.data.name} bearbeiten`
        })
    }

    public onSubmit() {
        this.form?.submit(this.api.updateOffice)
    }

    ngOnDestroy(): void {
        this.routeSub.unsubscribe()
    }
}
