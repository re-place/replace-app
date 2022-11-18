import {Component} from "@angular/core";
import {replace} from "replace";

@Component({
    selector: "app-root",
    template: "<p>Hello World</p>",
    styleUrls: ["./app.component.scss"]
})
export class AppComponent {
    title = "app";
    foo: replace.model.BookableEntity = new replace.model.BookableEntity("foo");
}
