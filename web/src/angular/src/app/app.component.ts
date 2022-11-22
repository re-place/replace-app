import {Component} from "@angular/core";
import {replace} from "replace";

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"]
})
export class AppComponent {
    title = "app";
    foo: replace.model.BookableEntity = new replace.model.BookableEntity("foo");
}
