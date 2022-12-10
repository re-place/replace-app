import { Component, Input } from "@angular/core"
import { ActivatedRoute, Router } from "@angular/router"

@Component({
    selector: "crud-layout [title]",
    templateUrl: "./crud-layout.component.html",
    styles: [],
})
export class CrudLayoutComponent {
    @Input() title!: string
    @Input() createRoute: string | null = null

    constructor(private readonly router: Router, private readonly activatedRoute: ActivatedRoute) {}

    public create() {
        if (this.createRoute === null) {
            return
        }

        // get root route from current module

        this.router.navigate([this.createRoute], { relativeTo: this.activatedRoute })
    }
}
