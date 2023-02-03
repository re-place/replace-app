import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChange, SimpleChanges } from "@angular/core"
import { Feature, Map, View } from "ol"
import { Point } from "ol/geom"
import ImageLayer from "ol/layer/Image"
import VectorLayer from "ol/layer/Vector"
import ImageStatic from "ol/source/ImageStatic"
import VectorSource from "ol/source/Vector"
import { Circle, Fill, Stroke, Style } from "ol/style"

import { BookableEntityDto, FileDto } from "src/app/core/openapi"
import { ImageLoader } from "src/app/util/ImageLoader"

export type Entity = {
    available: boolean
    entity: BookableEntityDto
    selected: boolean
}

const disabledStyle = new Style({
    image: new Circle({
        radius: 8,
        fill: new Fill({
            color: "#DBEAFE",
        }),
        stroke: new Stroke({
            color: "#9CA3AF",
            width: 2,
        }),
    }),
})

const selectedStyle = new Style({
    image: new Circle({
        radius: 8,
        fill: new Fill({
            color: "#EF4444",
        }),
        stroke: new Stroke({
            color: "rgba(255, 255, 255, 1)",
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

@Component({
    selector: "entity-map [entities] [backgroundImage]",
    templateUrl: "./entity-map.component.html",
    styles: [
    ],
})
export class EntityMapComponent implements OnInit, OnChanges {

    @Input() entities!: Entity[]

    @Input() backgroundImage: FileDto | undefined | null

    @Output() entitiesChange = new EventEmitter<Entity[]>()

    private readonly imageLayer = new ImageLayer({

    })

    private readonly entitySource = new VectorSource({
        features: [],
    })

    private readonly entityLayer = new VectorLayer({
        source: this.entitySource,
        style: (feature) => {
            const properties = feature.getProperties()

            if (properties["selected"] === true) {
                return selectedStyle
            }

            if (properties["available"] === true) {
                return availableStyle
            }

            return disabledStyle
        },
    })

    private map: Map | undefined
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

            map.updateSize()
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

    private createMap(): Map {
        const map = new Map({
            target: "map",
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

            const properties = feature.getProperties()

            if (properties["available"] === false) {
                return
            }

            const entityIndex = this.entities.findIndex(entity => entity.entity.id === feature.getId())

            if (entityIndex < 0) {
                return
            }

            const entity = this.entities[entityIndex]
            const newEntities = [...this.entities]
            newEntities.splice(entityIndex, 1, { ...entity, selected: !entity.selected })
            this.entitiesChange.emit(newEntities)
        })

        map.on("pointermove", (event) => {
            const feature = map.forEachFeatureAtPixel(event.pixel, (feature) => feature, {
                layerFilter: (layer) => layer === this.entityLayer,
            })

            if (feature === undefined) {
                map.getTargetElement().style.cursor = ""
                return
            }

            const properties = feature.getProperties()

            if (properties["available"] === false) {
                map.getTargetElement().style.cursor = ""
                return
            }

            map.getTargetElement().style.cursor = "pointer"
        })

        return map
    }

    public toFeatures(entities: Entity[]) {
        return entities.map(entity => {
            const feature = new Feature({
                geometry: new Point([entity.entity.posX ?? 0, entity.entity.posY ?? 0]),
                name: entity.entity.name,
                selected: entity.selected,
                available: entity.available,
            })

            feature.setId(entity.entity.id)

            return feature
        })
    }
}
