import { CommonModule } from "@angular/common"
import { HttpClientModule } from "@angular/common/http"
import { NgModule, Optional, SkipSelf } from "@angular/core"

import { AuthGuard } from "./guards/auth.guard"

@NgModule({
    declarations: [],
    imports: [CommonModule, HttpClientModule],
    providers: [AuthGuard],
})
export class CoreModule {
    constructor(@Optional() @SkipSelf() coreModule: CoreModule | null) {
        if (coreModule !== null) {
            throw new TypeError("Core Module is imported twice.")
        }
    }
}
