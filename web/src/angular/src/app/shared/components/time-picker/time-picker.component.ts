import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core"

@Component({
    selector: "time-picker",
    templateUrl: "./time-picker.component.html",
    styles: [
    ],
})
export class TimePickerComponent implements OnChanges {
    @Input() public date = new Date()

    @Input() public min: Date | undefined
    @Input() public max: Date | undefined

    @Output() public dateChange: EventEmitter<Date> = new EventEmitter()


    public get time() {
        return this.format(this.date)
    }

    public set time(value: string) {
        const [hours, minutes] = value.split(":").map(Number)

        const date = new Date(this.date)
        date.setHours(hours)
        date.setMinutes(minutes)
        date.setSeconds(0)
        date.setMilliseconds(0)

        if (this.min) {
            const minHours = this.min.getHours()
            const minMinutes = this.min.getMinutes()

            if (hours < minHours || (hours === minHours && minutes < minMinutes)) {
                date.setHours(minHours)
                date.setMinutes(minMinutes)
            }
        }

        if (this.max) {
            const maxHours = this.max.getHours()
            const maxMinutes = this.max.getMinutes()

            if (hours > maxHours || (hours === maxHours && minutes > maxMinutes)) {
                date.setHours(maxHours)
                date.setMinutes(maxMinutes)
            }
        }

        this.dateChange.emit(date)
    }

    public onInput(event: Event) {
        const input = event.target as HTMLInputElement

        if (!input.validity.valid) {
            return
        }

        this.time = input.value
    }

    ngOnChanges(changes: SimpleChanges): void {
        const minChange = changes["min"]
        const maxChange = changes["max"]

        if (minChange?.currentValue !== undefined) {
            const newMin = new Date(minChange.currentValue)

            if (this.date < newMin) {
                this.dateChange.emit(newMin)
            }
        }

        if (maxChange?.currentValue !== undefined) {
            const newMax = new Date(maxChange.currentValue)

            if (this.date > newMax) {
                this.dateChange.emit(newMax)
            }
        }
    }

    format(date: Date) {
        return `${date.getHours().toString().padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`
    }
}
