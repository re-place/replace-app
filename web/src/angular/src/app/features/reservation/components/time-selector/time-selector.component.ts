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
    styles: [
    ],
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

    @Output() intervalChange = new EventEmitter<Interval>()
    @Output() isSelectingTimeChange = new EventEmitter<boolean>()

    public readonly now = new Date()

    public dateTimeFormat: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

    private clickWasInside = false
    private complexView = false

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

        const start = new Date(this.start)
        const end = new Date(this.end)

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

        this.intervalChange.emit({ start, end })
    }

    public format: Intl.DateTimeFormatOptions = {
        dateStyle: "short",
        timeStyle: "short",
    }

    onClickInside() {
        this.clickWasInside = true
    }

    onClickOutside() {
        if (!this.isSelectingTime) {
            return
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

    set start(start: Date) {

        if (!this.isComplexView) {
            start.setHours(this.interval.start.getHours())
            start.setMinutes(this.interval.start.getMinutes())
            start.setSeconds(this.interval.start.getSeconds())
            start.setMilliseconds(this.interval.start.getMilliseconds())

            const end = new Date(this.end)

            end.setDate(start.getDate())
            end.setMonth(start.getMonth())
            end.setFullYear(start.getFullYear())

            this.intervalChange.emit({ start, end })
            return
        }

        this.intervalChange.emit({ ...this.interval, start })
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

    editingDateRange: DateRange<Date> | null = null

    get dateRange(): DateRange<Date> {
        return this.editingDateRange ??  new DateRange(this.start, this.end)
    }

    get startMinTime(): Date {
        if (
            this.interval.start.getDate() > this.now.getDate() ||
            this.interval.start.getMonth() > this.now.getMonth() ||
            this.interval.start.getFullYear() > this.now.getFullYear()
        ) {
            const min = new Date(this.now)

            min.setHours(0)
            min.setMinutes(0)
            min.setSeconds(0)
            min.setMilliseconds(0)

            return min
        }
        const min = new Date(this.now)

        min.setHours(min.getHours() + 1)
        min.setMinutes(0)

        return min
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
            start.setSeconds(this.interval.start.getSeconds())
            start.setMilliseconds(this.interval.start.getMilliseconds())

            date.setHours(this.interval.end.getHours())
            date.setMinutes(this.interval.end.getMinutes())
            date.setSeconds(this.interval.end.getSeconds())
            date.setMilliseconds(this.interval.end.getMilliseconds())

            this.intervalChange.emit({ start: this.editingDateRange.start, end: date })
            this.editingDateRange = null
            return
        }

        this.editingDateRange = new DateRange(date, null)
    }

}
