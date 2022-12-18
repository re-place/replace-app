import { Directive, TemplateRef } from "@angular/core"

@Directive({
    selector: "[footer]",
})
export class FooterDirective {
    constructor(public templateRef: TemplateRef<unknown>) {}
}
