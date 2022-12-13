import { HttpClient } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { firstValueFrom } from "rxjs"
import { BookableEntity, Floor, OfficeBuilding } from "types"

@Injectable({
    providedIn: "root",
})
export class ApiService {
    constructor(private readonly http: HttpClient) {}

    public async getOfficeBuildings() {
        return await firstValueFrom(this.http.get<OfficeBuilding[]>("/api/office-building"))
    }

    public async getOfficeBuilding(id: string) {
        return await firstValueFrom(this.http.get<OfficeBuilding>(`/api/office-building/${id}`))
    }

    public async createOfficeBuilding(office: Omit<OfficeBuilding, "_id">) {
        return await firstValueFrom(this.http.post<OfficeBuilding>("/api/office-building", office))
    }

    public async updateOfficeBuilding(office: OfficeBuilding) {
        return await firstValueFrom(this.http.put<OfficeBuilding>("/api/office-building/update", office))
    }

    public async getFloors(officeBuildingId?: string) {
        if (officeBuildingId === undefined) {
            return await firstValueFrom(this.http.get<Floor[]>("/api/floor"))
        }

        return await firstValueFrom(this.http.get<Floor[]>(`/api/office-building/${officeBuildingId}/floor`))
    }

    public async getFloor(id: string) {
        return await firstValueFrom(this.http.get<Floor>(`/api/floor/${id}`))
    }

    public async createFloor(floor: Omit<Floor, "_id">) {
        return await firstValueFrom(this.http.post<Floor>("/api/floor", floor))
    }

    public async updateFloor(floor: Floor) {
        return await firstValueFrom(this.http.put<Floor>("/api/floor/update", floor))
    }

    public async getBookableEntities(floorId?: string) {
        if (floorId === undefined) {
            return await firstValueFrom(this.http.get<BookableEntity[]>("/api/bookable-entity"))
        }

        return await firstValueFrom(this.http.get<BookableEntity[]>(`/api/floor/${floorId}/bookable-entity`))
    }

    public async getBookableEntity(id: string) {
        return await firstValueFrom(this.http.get<BookableEntity>(`/api/bookable-entity/${id}`))
    }

    public async createBookableEntity(entity: Omit<BookableEntity, "_id">) {
        return await firstValueFrom(this.http.post<BookableEntity>("/api/bookable-entity", entity))
    }

    public async updateBookableEntity(entity: BookableEntity) {
        return await firstValueFrom(this.http.put<BookableEntity>("/api/bookable-entity/update", entity))
    }
}
