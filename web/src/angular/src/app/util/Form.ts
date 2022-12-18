import { MatSnackBar } from "@angular/material/snack-bar"
import cloneDeep from "lodash/cloneDeep"
import { Subject, Subscription } from "rxjs"

export default class From<T> {
    protected _data: T
    protected _isProcessing = false
    protected _error: string | undefined = undefined

    public onError: Subject<string>

    protected snackBar: undefined | MatSnackBar
    protected snackBarSubscription: undefined | Subscription

    constructor(protected readonly initialData: T) {
        this._data = cloneDeep(initialData)

        this.onError = new Subject<string>()
    }

    public static from<T>(initialData: T) {
        return new From(initialData)
    }

    public useSnackbar(snackBar?: undefined | MatSnackBar) {
        if (this.snackBar) {
            this.snackBar.dismiss()
            this.snackBarSubscription?.unsubscribe()
        }

        this.snackBar = snackBar

        if (this.snackBar) {
            this.snackBarSubscription = this.onError.subscribe((error) => {
                this.snackBar?.open(error, "OK")
            })
        }
    }

    public get processing() {
        return this._isProcessing
    }

    public get error() {
        return this._error
    }

    public get data() {
        return this._data
    }

    public async submit<R>(submitTo: (data: T) => Promise<R>): Promise<R | undefined> {
        if (this._isProcessing) {
            return Promise.resolve(undefined)
        }

        this._isProcessing = true

        try {
            const response = await submitTo(this._data)
            this._error = undefined
            this._isProcessing = false
            return response
        } catch (error) {
            this._error = JSON.stringify(error)
            this._isProcessing = false
            this.onError.next(this._error)
            return undefined
        }
    }

    public reset() {
        this._isProcessing = false
        this._error = undefined
        this._data = cloneDeep(this.initialData)
    }
}
