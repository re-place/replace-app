import { Directive, TemplateRef } from "@angular/core"

@Directive({
    selector: "[icon]",
})
export class IconDirective {
    constructor(public templateRef: TemplateRef<unknown>) {}
}
