import { HttpClient, HttpRequest } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { firstValueFrom } from "rxjs"
import { BookableEntity, Floor, Site, TemporaryFileUpload } from "types"

import { randomString, urlForFile, urlForTemporaryFileUpload } from "src/app/util"

@Injectable({
    providedIn: "root",
})
export class ApiService {
    constructor(private readonly http: HttpClient) {}

    public async getSites() {
        return await firstValueFrom(this.http.get<Site[]>("/api/site"))
    }

    public async getSite(id: string) {
        return await firstValueFrom(this.http.get<Site>(`/api/site/${id}`))
    }

    public async createSite(site: Omit<Site, "id">) {
        return await firstValueFrom(this.http.post<Site>("/api/site", site))
    }

    public async updateSite(site: Site) {
        return await firstValueFrom(this.http.put<Site>("/api/site", site))
    }

    public async getFloors(siteId?: string) {
        if (siteId === undefined) {
            return await firstValueFrom(this.http.get<Floor[]>("/api/floor"))
        }

        return await firstValueFrom(this.http.get<Floor[]>(`/api/site/${siteId}/floor`))
    }

    public async getFloor(id: string) {
        return await firstValueFrom(this.http.get<Floor>(`/api/floor/${id}`))
    }

    public async createFloor(floor: Omit<Floor, "id">) {
        return await firstValueFrom(this.http.post<Floor>("/api/floor", floor))
    }

    public async updateFloor(floor: Floor) {
        return await firstValueFrom(this.http.put<Floor>("/api/floor", floor))
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

    public async createBookableEntity(entity: Omit<BookableEntity, "id">) {
        return await firstValueFrom(this.http.post<BookableEntity>("/api/bookable-entity", entity))
    }

    public async updateBookableEntity(entity: BookableEntity) {
        return await firstValueFrom(this.http.put<BookableEntity>("/api/bookable-entity", entity))
    }

    public createTemporaryFileUploads(files: File[]) {
        const formData = new FormData()

        for (const file of files) {
            formData.append(randomString(8), file, file.name)
        }

        const req = new HttpRequest("POST", "/api/temporary-file-upload", formData, {
            reportProgress: true,
        })

        return this.http.request<TemporaryFileUpload[]>(req)
    }

    public getTemporaryFileUpload(id: string) {
        const req = new HttpRequest("GET", urlForTemporaryFileUpload(id))
        return this.http.request<File>(req)
    }

    public deleteTemporaryFileUpload(id: string) {
        const req = new HttpRequest("DELETE", urlForTemporaryFileUpload(id))

        return this.http.request(req)
    }

    public getFile(id: string) {
        const req = new HttpRequest("GET", urlForFile(id))

        return this.http.request<File>(req)
    }
}
