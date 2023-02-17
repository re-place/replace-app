import { Pipe, PipeTransform } from "@angular/core"

@Pipe({
    name: "localeList",
})
export class LocaleListPipe implements PipeTransform {

    transform(value: null | Iterable<string> | undefined, options?: Intl.DateTimeFormatOptions | undefined): string | undefined {
        if (value === null || value === undefined) {
            return undefined
        }

        const formatter = new Intl.ListFormat(undefined, options)

        return formatter.format(value)
    }

}
