import cloneDeep from "lodash/cloneDeep"

export default class From<T> {
    protected _data: T
    protected _isProcessing = false
    protected _error: string | undefined = undefined

    constructor(protected readonly initialData: T) {
        this._data = cloneDeep(initialData)
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
            return undefined
        }
    }

    public reset() {
        this._isProcessing = false
        this._error = undefined
        this._data = cloneDeep(this.initialData)
    }
}
