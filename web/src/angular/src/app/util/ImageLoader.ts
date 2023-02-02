export type Subscription = (image: LoadedImage | undefined) => void
export type Unsubscribe = () => void

export type LoadedImage = {
    image: HTMLImageElement
    source: string
}

export class ImageLoader {
    private imageSource: string | undefined = undefined
    private subscriptions: Subscription[] = []
    private image: HTMLImageElement | undefined = undefined

    public setSource(imageSource: string | undefined) {
        this.imageSource = imageSource
        this.load()
        return this
    }
    public refresh() {
        this.load()
        return this
    }

    public getSource() {
        return this.imageSource
    }

    public getImage() {
        return this.image
    }

    public subscribe(subscription: Subscription): Unsubscribe {
        this.subscriptions.push(subscription)
        return this.unsubscribe.bind(this, subscription)
    }

    public unsubscribe(subscription: Subscription) {
        this.subscriptions = this.subscriptions.filter((s) => s !== subscription)
        return this
    }

    public clearSubscriptions() {
        this.subscriptions = []
        return this
    }

    private load() {
        const imageSource = this.imageSource

        if (imageSource === undefined) {
            this.image = undefined
            this.subscriptions.forEach((s) => s(undefined))
            return
        }

        const image = new Image()
        image.src = imageSource

        image.onload = () => {
            this.image = image
            this.subscriptions.forEach((s) => s({ image, source: imageSource }))
        }

        image.onerror = () => {
            this.image = undefined
            this.subscriptions.forEach((s) => s(undefined))
        }
    }
}
