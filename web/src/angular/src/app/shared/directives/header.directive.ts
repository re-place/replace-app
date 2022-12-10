import { Directive, TemplateRef } from "@angular/core"

@Directive({
    selector: "[header]",
})
export class HeaderDirective {
    constructor(public templateRef: TemplateRef<unknown>) {}
}
