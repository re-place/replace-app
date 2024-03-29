import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChange, SimpleChanges } from "@angular/core"
import { Feature, Map as OlMap, View } from "ol"
import { Point } from "ol/geom"
import ImageLayer from "ol/layer/Image"
import VectorLayer from "ol/layer/Vector"
import ImageStatic from "ol/source/ImageStatic"
import VectorSource from "ol/source/Vector"
import { Circle, Fill, Stroke, Style } from "ol/style"

import { BookableEntityDto, FileDto } from "src/app/core/openapi"
import { ImageLoader } from "src/app/util/ImageLoader"

export enum EntityStatus {
    AVAILABLE = "AVAILABLE",
    SELECTED = "SELECTED",
    BOOKED = "BOOKED",
    DISABLED = "DISABLED",
    SELECTED_DISABLED = "SELECTED_DISABLED",
}

export const interactableStates: (EntityStatus | undefined)[] = [EntityStatus.AVAILABLE, EntityStatus.SELECTED]

export type Entity = {
    status: EntityStatus
    entity: BookableEntityDto
}

const selectedStyle = new Style({
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
})

const selectedDisabledStyle = new Style({
    image: new Circle({
        radius: 10,
        fill: new Fill({
            color: "rgba(104, 172, 48, 0.3)",
        }),
        stroke: new Stroke({
            color: "rgba(255, 255, 255, 0.3)",
            width: 2,
        }),
    }),
})

const availableStyle = new Style({
    image: new Circle({
        radius: 8,
        fill: new Fill({
            color: "#1D4ED8",
        }),
        stroke: new Stroke({
            color: "rgba(0, 0, 0, 1)",
            width: 2,
        }),
    }),
})

const bookedStyle = new Style({
    image: new Circle({
        radius: 6,
        fill: new Fill({
            color: "#DBEAFE",
        }),
        stroke: new Stroke({
            color: "#9CA3AF",
            width: 2,
        }),
    }),
})

const disabledStyle = new Style({
    image: new Circle({
        radius: 8,
        fill: new Fill({
            color: "rgba(29,78,216, 0.3)",
        }),
        stroke: new Stroke({
            color: "rgba(0, 0, 0, 0.3)",
            width: 2,
        }),
    }),
})

@Component({
    selector: "entity-map [backgroundImage]",
    templateUrl: "./entity-map.component.html",
    styles: [],
})
export class EntityMapComponent implements OnInit, OnChanges {

    @Input() entities: Entity[] = []

    @Input() backgroundImage: FileDto | undefined | null

    @Output() entitySelected: EventEmitter<Entity> = new EventEmitter()

    private readonly imageLayer = new ImageLayer({

    })

    private readonly entitySource = new VectorSource({
        features: [],
    })

    private readonly entityLayer = new VectorLayer({
        source: this.entitySource,
        style: (feature) => {
            const status = feature.getProperties()["status"]

            switch (status) {
            case EntityStatus.SELECTED:
                return selectedStyle
            case EntityStatus.BOOKED:
                return bookedStyle
            case EntityStatus.DISABLED:
                return disabledStyle
            case EntityStatus.SELECTED_DISABLED:
                return selectedDisabledStyle
            default:
                return availableStyle
            }
        },
    })

    public map: OlMap | undefined
    private readonly imageLoader: ImageLoader = new ImageLoader()

    ngOnChanges(changes: SimpleChanges): void {
        const imageChanges = changes["backgroundImage"]
        this.onImageChange(imageChanges)

        const entitiesChange = changes["entities"]
        this.onEntitiesChange(entitiesChange)
    }

    onImageChange(imageChange: SimpleChange | undefined) {
        if (imageChange === undefined) {
            return
        }

        this.imageLoader.setSource(imageChange.currentValue?.url)
    }

    private onEntitiesChange(change: SimpleChange | undefined): void {
        if (change === undefined) {
            return
        }

        const entities = change.currentValue as Entity[]

        this.entitySource.clear()
        this.entitySource.addFeatures(this.toFeatures(entities))

        this.map?.updateSize()
    }


    ngOnInit(): void {
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
                padding: [10, 10, 10, 10],
            })
        })

        this.map = this.createMap()
    }

    private createMap(): OlMap {
        const map = new OlMap({
            target: "map",
            controls: [],
            layers: [
                this.imageLayer,
                this.entityLayer,
            ],
            view: new View({
                zoom: 2,
            }),
        })

        map.on("click", (event) => {
            const feature = map.forEachFeatureAtPixel(event.pixel, (feature) => feature, {
                layerFilter: (layer) => layer === this.entityLayer,
            })

            if (feature === undefined) {
                return
            }

            const status: EntityStatus | undefined = feature.getProperties()["status"]

            if (!interactableStates.includes(status)) {
                return
            }

            const entity = this.entities.find(entity => entity.entity.id === feature.getId())

            if (entity === undefined) {
                return
            }

            this.entitySelected.emit(entity)
        })

        map.on("pointermove", (event) => {
            const feature = map.forEachFeatureAtPixel(event.pixel, (feature) => feature, {
                layerFilter: (layer) => layer === this.entityLayer,
            })

            if (feature === undefined) {
                map.getTargetElement().style.cursor = ""
                return
            }

            const status: EntityStatus  = feature.getProperties()["status"]

            if (interactableStates.includes(status)) {
                map.getTargetElement().style.cursor = "pointer"
                return
            }

            map.getTargetElement().style.cursor = ""
        })

        return map
    }

    public toFeatures(entities: Entity[]) {
        return entities.map(entity => {
            const feature = new Feature({
                geometry: new Point([entity.entity.posX ?? 0, entity.entity.posY ?? 0]),
                name: entity.entity.name,
                status: entity.status,
            })


            feature.setId(entity.entity.id)

            return feature
        })
    }
}
