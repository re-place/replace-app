import { Pipe, PipeTransform } from "@angular/core"

@Pipe({
    name: "localeDateTime",
})
export class LocaleDateTimePipe implements PipeTransform {
    transform(value: null | string | undefined | Date, options?: Intl.DateTimeFormatOptions | undefined): string | undefined {
        if (value === null || value === undefined) {
            return undefined
        }

        if (typeof value === "string") {
            return new Date(value).toLocaleString(undefined, options)
        }

        return value.toLocaleString(undefined, options)
    }
}
