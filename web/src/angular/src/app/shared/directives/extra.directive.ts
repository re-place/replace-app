import { Directive, TemplateRef } from "@angular/core"

@Directive({
    selector: "[extra]",
})
export class ExtraDirective {
    constructor(public templateRef: TemplateRef<unknown>) {}
}
