import { AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChange, SimpleChanges, ChangeDetectorRef } from "@angular/core"
import { Feature, Map as OlMap, View } from "ol"
import { Point } from "ol/geom"
import ImageLayer from "ol/layer/Image"
import VectorLayer from "ol/layer/Vector"
import ImageStatic from "ol/source/ImageStatic"
import VectorSource from "ol/source/Vector"
import { Circle, Fill, Stroke, Style } from "ol/style"

import { Booking } from "../../pages/reservation-overview/reservation-overview.component"
import { BookableEntityDto } from "src/app/core/openapi"
import { ImageLoader } from "src/app/util/ImageLoader"

@Component({
    selector: "booking-map",
    templateUrl: "./booking-map.component.html",
    styles: [],
})
export class BookingMapComponent implements OnChanges, OnInit, AfterViewInit {
    @Input() booking: Booking | undefined

    private readonly imageLayer = new ImageLayer()

    private readonly entitySource = new VectorSource({
        features: [],
    })

    public id = "booking-map"

    private readonly entityLayer = new VectorLayer({
        source: this.entitySource,
        style: new Style({
            image: new Circle({
                radius: 10,
                fill: new Fill({
                    color: "#68ac30",
                }),
                stroke: new Stroke({
                    color: "rgba(255, 255, 255, 1)",
                    width: 2,
                }),
            }),
        }),
    })

    public constructor(private readonly ch: ChangeDetectorRef) {}

    public map: OlMap | undefined
    private readonly imageLoader: ImageLoader = new ImageLoader()

    ngOnChanges(changes: SimpleChanges): void {
        const bookingChange = changes["booking"]
        this.onBookingChange(bookingChange)
    }

    private onBookingChange(change: SimpleChange | undefined): void {
        if (change === undefined) {
            return
        }

        const booking = change.currentValue as Booking | undefined | null

        this.entitySource.clear()

        if (booking === undefined || booking === null) {
            this.imageLoader.setSource(undefined)
            this.map?.updateSize()
            return
        }

        this.entitySource.addFeatures(this.toFeatures(booking.bookedEntities))
        this.imageLoader.setSource(booking.floor.planFile?.url)

        this.map?.updateSize()
    }


    ngOnInit(): void {
        this.id = `booking-map-${this.booking?.id ?? "undefined"}`
        this.imageLoader.clearSubscriptions()
        this.imageLoader.subscribe((image) => {
            if (image === undefined) {
                this.imageLayer.setSource(null)
                return
            }

            const extent = [0, 0, image.image.width, image.image.height]

            this.imageLayer.setSource(new ImageStatic({
                url: image.source,
                imageExtent: extent,
                imageLoadFunction: (imageToLoad) => {
                    imageToLoad.setImage(image.image)
                },
            }))

            const map = this.map

            if (map === undefined) {
                return
            }

            const size = map.getSize()

            if (size === undefined) {
                return
            }

            const view = map.getView()

            view.fit(extent, {
                size,

            })
        })
    }

    ngAfterViewInit(): void {
        this.map = this.createMap()
        this.ch.detectChanges()
    }


    private createMap(): OlMap {
        const map = new OlMap({
            target: this.id,
            controls: [],
            interactions: [],
            layers: [
                this.imageLayer,
                this.entityLayer,
            ],
            view: new View({
                zoom: 2,
            }),
        })

        return map
    }

    public toFeatures(entities: BookableEntityDto[]) {
        return entities.map(entity => {
            const feature = new Feature({
                geometry: new Point([entity.posX ?? 0, entity.posY ?? 0]),
            })

            feature.setId(entity.id)

            return feature
        })
    }
}
