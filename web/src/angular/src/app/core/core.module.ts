import { CommonModule } from "@angular/common"
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http"
import { NgModule, Optional, SkipSelf } from "@angular/core"

import { AuthGuard } from "./guards/auth.guard"
import { NotAuthenticatedRedirect } from "./interceptors/not-authenticated-redirect.interceptor"

@NgModule({
    declarations: [],
    imports: [CommonModule, HttpClientModule],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotAuthenticatedRedirect,
            multi: true,
        },
        AuthGuard],
})
export class CoreModule {
    constructor(@Optional() @SkipSelf() coreModule: CoreModule | null) {
        if (coreModule !== null) {
            throw new TypeError("Core Module is imported twice.")
        }
    }
}
