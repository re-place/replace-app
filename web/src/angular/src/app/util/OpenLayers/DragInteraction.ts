import { MapBrowserEvent } from "ol"
// eslint-disable-next-line import/named
import { Coordinate } from "ol/coordinate"
// eslint-disable-next-line import/named
import { FeatureLike } from "ol/Feature"
import { Geometry } from "ol/geom"
import PointerInteraction from "ol/interaction/Pointer"
import Layer from "ol/layer/Layer"
import LayerRenderer from "ol/renderer/Layer"
import Source from "ol/source/Source"

export type LayerFilter = (layer: Layer<Source, LayerRenderer<any>>) => boolean

export type DragInteractionOptions = {
    layerFilter?: LayerFilter
    cursor?: string
}

export type DragEndSubscriber = (feature: FeatureLike, deltaX: number, deltaY: number) => void
export type DragSubscriber = (feature: FeatureLike, deltaX: number, deltaY: number) => void | boolean
export type Unsubscribe = () => void
/**
 * Inspired by https://openlayers.org/en/latest/examples/custom-interactions.html
 */
export class DragInteraction extends PointerInteraction {

    private readonly layerFilter: LayerFilter | undefined = undefined

    private readonly cursor: string = "grab"
    private readonly cursorGrabbing: string = "grabbing"

    private originalCoordinates: Coordinate | undefined
    private coordinates: Coordinate | undefined

    private feature: FeatureLike | undefined

    private previousCursor: string | undefined = undefined

    private dragEndSubscribers: DragEndSubscriber[] = []
    private dragSubscribers: DragSubscriber[] = []

    constructor(options?: DragInteractionOptions) {
        super()

        this.layerFilter = options?.layerFilter
    }

    public subscribeDragEnd(subscriber: DragEndSubscriber): Unsubscribe {
        this.dragEndSubscribers.push(subscriber)

        return this.unsubscribeDragEnd.bind(this, subscriber)
    }

    public subscribeDrag(subscriber: DragSubscriber): Unsubscribe {
        this.dragSubscribers.push(subscriber)

        return this.unsubscribeDrag.bind(this, subscriber)
    }

    public unsubscribeDrag(subscriber: DragSubscriber) {
        this.dragSubscribers = this.dragSubscribers.filter((s) => s !== subscriber)
    }

    public unsubscribeDragEnd(subscriber: DragEndSubscriber) {
        this.dragEndSubscribers = this.dragEndSubscribers.filter((s) => s !== subscriber)
    }

    public clearDragEndSubscribers() {
        this.dragEndSubscribers = []
    }

    public clearDragSubscribers() {
        this.dragSubscribers = []
    }

    override handleDownEvent(event: MapBrowserEvent<UIEvent>) {
        const map = event.map

        const feature = map.forEachFeatureAtPixel(event.pixel, function (feature) {
            return feature
        }, {
            layerFilter: this.layerFilter,
        })

        if (feature !== undefined) {
            this.originalCoordinates = event.coordinate
            this.coordinates = event.coordinate
            this.feature = feature
            map.getTargetElement().style.cursor = this.cursorGrabbing
        }

        return feature !== undefined
    }

    override handleDragEvent(event: MapBrowserEvent<UIEvent>) {
        const feature = this.feature

        if (feature == undefined || this.coordinates === undefined) {
            return
        }

        const map = event.map
        map.getTargetElement().style.cursor = this.cursorGrabbing

        const deltaX = Math.round(event.coordinate[0] - this.coordinates[0])
        const deltaY = Math.round(event.coordinate[1] - this.coordinates[1])

        const geometry = feature.getGeometry()

        if (geometry === undefined) {
            return
        }

        if (!this.dragSubscribers.reduce<boolean>((agg, sub) => agg || sub(feature, deltaX, deltaY) === false, false)) {
            (geometry as Geometry).translate(deltaX, deltaY)
        }

        this.coordinates[0] = event.coordinate[0]
        this.coordinates[1] = event.coordinate[1]
    }

    override handleMoveEvent(event: MapBrowserEvent<UIEvent>) {
        if (this.feature !== undefined) {
            return
        }

        const map = event.map

        const feature = map.forEachFeatureAtPixel(event.pixel, function (feature) {
            return feature
        }, {
            layerFilter: this.layerFilter,
        })

        const element = event.map.getTargetElement()

        if (feature !== undefined) {
            if (element.style.cursor != this.cursor) {
                this.previousCursor = element.style.cursor
                element.style.cursor = this.cursor
            }

            return
        }

        if (this.previousCursor !== undefined) {
            element.style.cursor = this.previousCursor
            this.previousCursor = undefined
        }
    }

    override handleUpEvent(event: MapBrowserEvent<UIEvent>) {
        const feature = this.feature

        if (feature !== undefined && this.originalCoordinates !== undefined && this.coordinates !== undefined) {
            const deltaX = this.coordinates[0] - this.originalCoordinates[0]
            const deltaY = this.coordinates[1] - this.originalCoordinates[1]

            this.dragEndSubscribers.forEach((subscriber) => subscriber(feature, deltaX, deltaY))

            const map = event.map
            map.getTargetElement().style.cursor = this.cursor
        }

        this.coordinates = undefined
        this.feature = undefined
        return false
    }


}
