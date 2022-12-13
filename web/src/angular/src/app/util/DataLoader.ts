export default class DataLoader<T> {
    protected _data: undefined | T
    protected _processing = false
    protected _dataSource: (() => Promise<T>) | undefined

    public constructor(dataSource?: () => Promise<T>) {
        this._dataSource = dataSource
    }

    public static from<T>(dataSource?: () => Promise<T>) {
        return new DataLoader(dataSource)
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
