import { SimpleChange , Component, Input, OnChanges, OnInit, SimpleChanges, Output, EventEmitter } from "@angular/core"
import { Feature, View } from "ol"
import { Point } from "ol/geom"
import ImageLayer from "ol/layer/Image"
import VectorLayer from "ol/layer/Vector"
import Map from "ol/Map.js"
import ImageStatic from "ol/source/ImageStatic"
import VectorSource from "ol/source/Vector"
import { Circle, Fill, Stroke } from "ol/style"
import Style from "ol/style/Style"


import { BookableEntityDto, FileDto } from "src/app/core/openapi"
import { ImageLoader } from "src/app/util/ImageLoader"
import { DragInteraction } from "src/app/util/OpenLayers/DragInteraction"
@Component({
    selector: "entity-map [entities] [backgroundImage]",
    templateUrl: "./entity-map.component.html",
    styles: [],
})
export class EntityMapComponent implements OnInit, OnChanges {
    @Input() entities!: BookableEntityDto[]
    @Input() selectedEntity: BookableEntityDto | undefined
    @Input() backgroundImage: FileDto | undefined | null

    @Output() selectedEntityChange = new EventEmitter<BookableEntityDto>()
    @Output() entityDrag = new EventEmitter<{ entity: BookableEntityDto, deltaX: number, deltaY: number }>()

    private readonly imageLayer = new ImageLayer({

    })

    private readonly unselectedEntitySource = new VectorSource({
        features: [],
    })

    private readonly unselectedEntityLayer = new VectorLayer({
        source: this.unselectedEntitySource,
        style: new Style({
            image: new Circle({
                radius: 10,
                fill: new Fill({
                    color: "rgba(0, 0, 255, 1)",
                }),
                stroke: new Stroke({
                    color: "rgba(0, 0, 0, 1)",
                    width: 2,
                }),
            }),
        }),
    })

    private readonly selectedEntitySource = new VectorSource({
        features: [],
    })

    private readonly selectedEntityLayer = new VectorLayer({
        source: this.selectedEntitySource,
        style: new Style({
            image: new Circle({
                radius: 13,
                fill: new Fill({
                    color: "rgba(255, 0, 0, 1)",
                }),
                stroke: new Stroke({
                    color: "rgba(255, 255, 255, 1)",
                    width: 2,
                }),
            }),
        }),
    })

    private map: Map | undefined
    private readonly imageLoader: ImageLoader = new ImageLoader()

    private readonly dragInteraction = new DragInteraction({
        layerFilter: (layer) => layer === this.selectedEntityLayer,
    })


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

            this.map?.getView().fit(extent)
        })

        this.map = this.createMap()

        this.dragInteraction.clearDragEndSubscribers()
        this.dragInteraction.subscribeDrag((feature, deltaX, deltaY) => {
            if (this.selectedEntity === undefined) {
                return
            }

            this.entityDrag.emit({
                entity: this.selectedEntity,
                deltaX,
                deltaY,
            })
        })
    }

    ngOnChanges(changes: SimpleChanges): void {
        const imageChanges = changes["backgroundImage"]
        this.onImageChange(imageChanges)

        const entityChanges = changes["entities"]
        this.onEntityChange(entityChanges)

        const selectedEntityChanges = changes["selectedEntity"]
        this.onSelectedEntityChange(selectedEntityChanges)

    }

    onImageChange(imageChange: SimpleChange | undefined) {
        if (imageChange === undefined) {
            return
        }

        this.imageLoader.setSource(imageChange.currentValue?.url)
    }

    onEntityChange(entityChange: SimpleChange | undefined) {
        if (entityChange === undefined) {
            return
        }

        this.unselectedEntitySource.clear()
        this.unselectedEntitySource.addFeatures(this.getUnselectedFeatures(entityChange.currentValue))
    }

    public onSelectedEntityChange(entityChange: SimpleChange | undefined) {
        if (entityChange === undefined) {
            return
        }

        const entity = entityChange.currentValue

        this.selectedEntitySource.clear()
        this.unselectedEntitySource.clear()
        this.unselectedEntitySource.addFeatures(this.getUnselectedFeatures(this.entities))

        if (entity === undefined) {
            return
        }

        const feature = new Feature({
            geometry: new Point([entity.posX ?? 0, entity.posY ?? 0]),
        })

        feature.setId(entity.id)

        this.selectedEntitySource.addFeature(feature)
    }


    private createMap(): Map {
        const map = new Map({
            target: "map",
            layers: [
                this.imageLayer,
                this.unselectedEntityLayer,
                this.selectedEntityLayer,
            ],
            view: new View({
                zoom: 2,
            }),
        })

        map.on("click", (event) => {
            const feature = map.forEachFeatureAtPixel(event.pixel, (feature) => feature, {
                layerFilter: (layer) => layer === this.unselectedEntityLayer,
            })

            if (feature === undefined) {
                return
            }

            const entity = this.entities.find(entity => entity.id === feature.getId())

            if (entity === undefined) {
                return
            }
            this.selectedEntityChange.emit(entity)
        })

        map.on("pointermove", (event) => {
            const feature = map.forEachFeatureAtPixel(event.pixel, (feature) => feature, {
                layerFilter: (layer) => layer === this.unselectedEntityLayer,
            })

            if (feature === undefined) {
                map.getTargetElement().style.cursor = ""
                return
            }

            map.getTargetElement().style.cursor = "pointer"
        })

        map.addInteraction(this.dragInteraction)

        return map
    }

    private getUnselectedFeatures(entities: BookableEntityDto[]): Feature[] {
        return entities
            .filter(entity => entity.id !== this.selectedEntity?.id)
            .map(entity => {
                const feature = new Feature({
                    geometry: new Point([entity.posX ?? 0, entity.posY ?? 0]),
                })

                feature.setId(entity.id)

                return feature
            })
    }
}
