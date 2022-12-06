import { CommonModule } from "@angular/common"
import { NgModule, Optional, SkipSelf } from "@angular/core"

@NgModule({
    declarations: [],
    imports: [CommonModule],
})
export class CoreModule {
    constructor(@Optional() @SkipSelf() coreModule: CoreModule | null) {
        if (coreModule !== null) {
            throw new TypeError("Core Module is imported twice.")
        }
    }
}
