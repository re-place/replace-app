import { Pipe, PipeTransform } from "@angular/core"

@Pipe({
    name: "localeDateTimeRange",
})
export class LocaleDateTimeRangePipe implements PipeTransform {

    transform(
        value: [null | string | undefined | Date, null | string | undefined | Date],
        options?: Intl.DateTimeFormatOptions | undefined,
    ): string | undefined {
        const start = value[0]
        const end = value[1]

        if (start === null || start === undefined || end === null || end === undefined) {
            return undefined
        }

        const startDate = new Date(start)
        const endDate = new Date(end)

        return Intl.DateTimeFormat(undefined, options).formatRange(startDate, endDate)
    }
}
