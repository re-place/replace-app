import { Pipe, PipeTransform } from "@angular/core"

@Pipe({
    name: "localeDate",
})
export class LocaleDatePipe implements PipeTransform {

    transform(value: null | string | undefined | Date, options?: Intl.DateTimeFormatOptions | undefined): string | undefined {
        if (value === null || value === undefined) {
            return undefined
        }

        if (typeof value === "string") {
            return new Date(value).toLocaleDateString(undefined, options)
        }

        return value.toLocaleDateString(undefined, options)
    }

}
