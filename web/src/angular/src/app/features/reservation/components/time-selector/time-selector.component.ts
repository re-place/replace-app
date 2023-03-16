import { Component, EventEmitter, HostListener, Input, Output } from "@angular/core"
import { MAT_DATE_RANGE_SELECTION_STRATEGY, DefaultMatCalendarRangeStrategy, DateRange } from "@angular/material/datepicker"

export type Interval = {
    start: Date
    end: Date
}

type TimePeriod = "day" | "morning" | "afternoon"

@Component({
    selector: "time-selector [interval]",
    templateUrl: "./time-selector.component.html",
    styles: [],
    providers: [
        {
            provide: MAT_DATE_RANGE_SELECTION_STRATEGY,
            useClass: DefaultMatCalendarRangeStrategy,
        },
    ],

})
export class TimeSelectorComponent {

    @Input() interval!: Interval
    @Input() isSelectingTime!: boolean
    @Input() disabled = false

    @Output() intervalChange = new EventEmitter<Interval>()
    @Output() isSelectingTimeChange = new EventEmitter<boolean>()

    public readonly now = new Date()

    public dateTimeFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

    private clickWasInside = false
    private complexView = false

    get minDate() {
        if (this.now.getHours() < 18) {
            return this.now
        }

        const min = new Date(this.now)
        min.setDate(min.getDate() + 1)
        min.setHours(0)
        min.setMinutes(0)
        min.setSeconds(0)
        min.setMilliseconds(0)

        return min
    }

    get disableMorning () {

        if (this.isComplexView) {
            return false
        }

        if (this.isOnSameDay(this.start, this.now)) {
            return this.now.getHours() > 12
        }

        return false
    }

    get isComplexView () {
        return this.complexView
    }

    set isComplexView (value: boolean) {
        this.complexView = value

        if (value) {
            return
        }


        const start = new Date(this.start)
        const end = new Date(this.end)

        // eslint-disable-next-line @typescript-eslint/prefer-optional-chain
        if (this.editingDateRange !== null && this.editingDateRange.start !== null) {
            start.setDate(this.editingDateRange.start.getDate())
            start.setMonth(this.editingDateRange.start.getMonth())
            start.setFullYear(this.editingDateRange.start.getFullYear())

            this.editingDateRange = null
        }

        end.setDate(start.getDate())
        end.setMonth(start.getMonth())
        end.setFullYear(start.getFullYear())

        start.setMinutes(0)
        start.setSeconds(0)
        start.setMilliseconds(0)

        end.setMinutes(0)
        end.setSeconds(0)
        end.setMilliseconds(0)

        const startHours = start.getHours()
        const endHours = end.getHours()


        if (startHours < 8) {
            start.setHours(8)
        }

        if (startHours > 12) {
            start.setHours(12)
        }

        if (8 < startHours && startHours < 12) {
            start.setHours(8)
        }

        if (endHours < 12) {
            end.setHours(12)
        }

        if (endHours > 18) {
            end.setHours(18)
        }

        if (12 < endHours && endHours < 18) {
            end.setHours(18)
        }

        if (startHours === 12 && endHours === 12) {
            end.setHours(18)
        }

        if (end < this.minDate) {
            start.setDate(start.getDate() + 1)
            end.setDate(end.getDate() + 1)
        }

        this.intervalChange.emit({ start, end })
    }

    get selectedTimePeriod(): TimePeriod | undefined {
        const start = this.start
        const end = this.end

        if (start.getDate() !== end.getDate()) {
            return undefined
        }

        if (start.getMonth() !== end.getMonth()) {
            return undefined
        }

        if (start.getFullYear() !== end.getFullYear()) {
            return undefined
        }

        if (start.getHours() === 8 && end.getHours() === 18) {
            return "day"
        }

        if (start.getHours() === 8 && end.getHours() === 12) {
            return "morning"
        }

        if (start.getHours() === 12 && end.getHours() === 18) {
            return "afternoon"
        }

        return undefined
    }

    set selectedTimePeriod(period: TimePeriod | undefined) {
        if (period === undefined) {
            return
        }

        this.intervalChange.emit(this.getTimesForTimePeriod(period, this.start, this.end))
    }

    getTimesForTimePeriod(period: TimePeriod, startDate: Date, endDate: Date): { start: Date, end: Date } {

        const start = new Date(startDate)
        const end = new Date(endDate)

        start.setSeconds(0)
        start.setMilliseconds(0)
        end.setSeconds(0)
        end.setMilliseconds(0)

        end.setDate(start.getDate())
        end.setMonth(start.getMonth())
        end.setFullYear(start.getFullYear())

        start.setMinutes(0)
        end.setMinutes(0)

        if (period === "day") {
            start.setHours(8)
            end.setHours(18)
        } else if (period === "morning") {
            start.setHours(8)
            end.setHours(12)
        } else if (period === "afternoon") {
            start.setHours(12)
            end.setHours(18)
        }

        return { start, end }
    }

    public format: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

    onClickInside() {
        this.clickWasInside = true
    }

    onClickOutside(event?: Event) {
        if (!this.isSelectingTime) {
            return
        }

        event?.stopPropagation()

        if (this.isComplexView && this.editingDateRange !== null) {
            const start = this.editingDateRange.start ?? this.start
            const end = new Date(start)

            start.setHours(this.interval.start.getHours())
            start.setMinutes(this.interval.start.getMinutes())
            start.setSeconds(0)
            start.setMilliseconds(0)

            end.setHours(this.interval.end.getHours())
            end.setMinutes(this.interval.end.getMinutes())
            end.setSeconds(0)
            end.setMilliseconds(0)

            if (start === end || start > end) {
                end.setHours(start.getHours() + 1)
            }

            this.intervalChange.emit({ start, end })

            this.editingDateRange = null
        }

        this.isSelectingTimeChange.emit(false)
    }
    openTimeSelector() {
        this.isSelectingTimeChange.emit(true)
    }

    @HostListener("click", ["$event"])
    onClick() {
        this.onClickInside()
    }

    @HostListener("document:keydown.escape", ["$event"])
    onKeydownHandler() {
        this.onClickOutside()
    }

    @HostListener("document:click", ["$event"])
    onClickDocument() {
        if (this.clickWasInside) {
            this.clickWasInside = false
            return
        }

        this.onClickOutside()
    }

    isOnSameDay(date1: Date, date2: Date) {
        return (
            date1.getDate() === date2.getDate() &&
            date1.getMonth() === date2.getMonth() &&
            date1.getFullYear() === date2.getFullYear()
        )
    }

    set start(start: Date) {

        if (this.isComplexView) {
            this.intervalChange.emit({ ...this.interval, start })
            return
        }

        start.setHours(this.interval.start.getHours())
        start.setMinutes(this.interval.start.getMinutes())
        start.setSeconds(0)
        start.setMilliseconds(0)

        const end = new Date(this.end)

        end.setDate(start.getDate())
        end.setMonth(start.getMonth())
        end.setFullYear(start.getFullYear())

        if (end < this.now) {
            this.intervalChange.emit(this.getTimesForTimePeriod("afternoon", start, end))
            return
        }

        this.intervalChange.emit({ start, end })
    }

    get start() {
        return this.interval.start
    }

    get end() {
        return this.interval.end
    }

    set end(date: Date) {
        this.intervalChange.emit({ ...this.interval, end: date })
    }

    get startMax() {
        if (this.isOnSameDay(this.start, this.end)) {
            return this.end
        }

        return undefined
    }

    get startMin() {
        if (this.isOnSameDay(this.start, this.now)) {
            const min = new Date(this.now)
            min.setHours(min.getHours() + 1)
            min.setMinutes(0)
            min.setSeconds(0)
            min.setMilliseconds(0)
            return min
        }

        return undefined
    }

    get endMin() {
        if (this.isOnSameDay(this.end, this.start)) {
            const min = new Date(this.start)
            min.setMinutes(min.getMinutes() + 1)
            return min
        }

        return undefined
    }

    editingDateRange: DateRange<Date> | null = null

    get dateRange(): DateRange<Date> {
        return this.editingDateRange ??  new DateRange(this.start, this.end)
    }

    onSelectedChange(date: Date): void {
        if (!this.editingDateRange) {
            this.editingDateRange = new DateRange(date, null)
            return
        }

        if (this.editingDateRange.start && this.editingDateRange.start > date) {
            this.editingDateRange = new DateRange(date, null)
            return
        }

        if (this.editingDateRange.start && !this.editingDateRange.end) {
            const start = this.editingDateRange.start

            start.setHours(this.interval.start.getHours())
            start.setMinutes(this.interval.start.getMinutes())
            start.setSeconds(0)
            start.setMilliseconds(0)

            date.setHours(this.interval.end.getHours())
            date.setMinutes(this.interval.end.getMinutes())
            date.setSeconds(0)
            date.setMilliseconds(0)

            this.intervalChange.emit({ start: this.editingDateRange.start, end: date })
            this.editingDateRange = null
            return
        }

        this.editingDateRange = new DateRange(date, null)
    }

}
