export default class DataLoader<T> {
    protected _data: undefined | T
    protected _processing = false
    protected _dataSource: (() => Promise<T>) | undefined
    protected _subscriptions: ((data: T) => void)[] = []

    public constructor(dataSource?: () => Promise<T>) {
        this._dataSource = dataSource
    }

    public static from<T>(dataSource?: () => Promise<T>) {
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

    public source(dataSource: () => Promise<T>) {
        this._dataSource = dataSource
        return this
    }

    public refresh() {
        if (this._dataSource === undefined) {
            this._processing = false
            return
        }

        this._processing = true

        this._dataSource()
            .then((data) => {
                this._data = data
            })
            .finally(() => {
                this._processing = false
                this._subscriptions.forEach((s) => s(this._data as T))
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
