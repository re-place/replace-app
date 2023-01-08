import { Observable } from "rxjs"

export type DataSource<T> = (() => Promise<T> | Observable<T>)

export default class DataLoader<T> {
    protected _data: undefined | T
    protected _processing = false
    protected _dataSource: DataSource<T> | undefined
    protected _subscriptions: ((data: T) => void)[] = []

    public constructor(dataSource?: DataSource<T>) {
        this._dataSource = dataSource
    }

    public static from<T>(dataSource?: DataSource<T>) {
        return new DataLoader(dataSource)
    }

    public subscribe(subscription: (data: T) => void): () => void {
        this._subscriptions.push(subscription)
        return this.unsubscribe.bind(this, subscription)
    }

    public unsubscribe(subscription: (data: T) => void) {
        this._subscriptions = this._subscriptions.filter((s) => s !== subscription)
        return this
    }

    public source(dataSource: DataSource<T>) {
        this._dataSource = dataSource
        return this
    }

    public refresh() {
        if (this._dataSource === undefined) {
            this._processing = false
            return
        }

        this._processing = true

        const source = this._dataSource()

        if (source instanceof Promise) {
            source
                .then((data) => {
                    this._data = data
                    this._subscriptions.forEach((s) => s(data))
                })
                .catch((e) => {
                    console.error("Error while loading data", e)
                })
                .finally(() => {
                    this._processing = false
                })
            return this
        }

        source.subscribe({
            next: (data) => {
                this._data = data
                this._subscriptions.forEach((s) => s(data))
            },
            error: (e) => {
                console.error("Error while loading data", e)
            },
            complete: () => {
                this._processing = false
            },
        })

        return this
    }

    public get processing() {
        return this._processing
    }

    public get data() {
        return this._data
    }

    public loading(): boolean
    public loading(loading: boolean): this

    public loading(loading?: boolean) {
        if (loading === undefined) {
            return this._processing
        }

        return this
    }
}
