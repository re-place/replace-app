import { Component, ContentChild, Input, TemplateRef } from "@angular/core"

type Colors = "primary"

const buttonStyles: Record<Colors, string> = {
    primary: "text-green-500  hover:text-green-700 hover:bg-black/5",
}

@Component({
    selector: "text-button",
    templateUrl: "./text-button.component.html",
    styles: [],
})
export class TextButtonComponent {
    @Input() element: "a" | "button" | "routerLink" = "button"
    @Input() href: string | undefined
    @Input() target: string | undefined

    @Input() color: Colors = "primary"

    @Input() disabled = false

    @Input() type: "button" | "submit" | "reset" = "button"

    @ContentChild(TemplateRef) content: TemplateRef<unknown> | undefined

    get colorStyle() {
        return buttonStyles[this.color]
    }
}
