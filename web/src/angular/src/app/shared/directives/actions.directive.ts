import { Directive, TemplateRef } from "@angular/core"

@Directive({
    selector: "[actions]",
})
export class ActionsDirective {
    constructor(public templateRef: TemplateRef<unknown>) {}
}
