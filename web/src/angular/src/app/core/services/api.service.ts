import { HttpClient } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { firstValueFrom } from "rxjs"
import { Floor, Office } from "types"

@Injectable({
    providedIn: "root",
})
export class ApiService {
    constructor(private readonly http: HttpClient) {}

    public async getOffices() {
        return await firstValueFrom(this.http.get<Office[]>("/api/office"))
    }

    public async getOffice(id: string) {
        return await firstValueFrom(this.http.get<Office>(`/api/office/${id}`))
    }

    public async createOffice(office: Omit<Office, "_id">) {
        return await firstValueFrom(this.http.post<Office>("/api/office", office))
    }

    public async updateOffice(office: Office) {
        return await firstValueFrom(this.http.put<Office>(`/api/office/${office._id}`, office))
    }

    public async getFloors(officeId?: string) {
        if (officeId === undefined) {
            return await firstValueFrom(this.http.get<Floor[]>("/api/floor"))
        }

        return await firstValueFrom(this.http.get<Floor[]>(`/api/office/${officeId}/floor`))
    }

    public async getFloor(id: string) {
        return await firstValueFrom(this.http.get<Floor>(`/api/floor/${id}`))
    }

    public async createFloor(floor: Omit<Floor, "_id">) {
        return await firstValueFrom(this.http.post<Floor>("/api/floor", floor))
    }

    public async updateFloor(floor: Floor) {
        return await firstValueFrom(this.http.put<Floor>(`/api/floor/${floor._id}`, floor))
    }
}
