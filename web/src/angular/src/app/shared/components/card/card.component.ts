import { Component, ContentChild, Input } from "@angular/core"

import { FooterDirective } from "../../directives/footer.directive"
import { HeaderDirective } from "../../directives/header.directive"

@Component({
    selector: "card",
    templateUrl: "./card.component.html",
    styles: [],
})
export class CardComponent {
    @Input() grayFooter = false
    @Input() expandOnMobile = false

    @ContentChild(HeaderDirective) header: HeaderDirective | undefined
    @ContentChild(FooterDirective) footer: FooterDirective | undefined
}
