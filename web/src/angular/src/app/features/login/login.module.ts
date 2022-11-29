import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { FormsModule } from "@angular/forms"

import { LoginRoutingModule } from "./login-routing.module"
import { LoginComponent } from "./pages/login/login.component"
import { MaterialModule } from "src/app/material/material.module"


@NgModule({
    declarations: [
        LoginComponent,
    ],
    imports: [
        CommonModule,
        LoginRoutingModule,
        MaterialModule,
        FormsModule,
    ],
})
export class LoginModule { }
