import { HttpClient } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { Router } from "@angular/router"
import { firstValueFrom } from "rxjs"
import { User } from "types"

@Injectable({
    providedIn: "root",
})
export class AuthService {
    constructor(private readonly http: HttpClient, private readonly router: Router) {}
    currentUser: User | null = null

    public async login(username: string, password: string) {
        this.currentUser = await firstValueFrom(this.http.post<User>("/api/login", { username, password }))

        return this.currentUser
    }

    public async isAuthenticated(): Promise<boolean> {
        if (this.currentUser !== null) {
            return true
        }

        try {
            this.currentUser = await firstValueFrom(this.http.get<User>("/api/current-user"))
            return true
        } catch (error) {
            return false
        }
    }

    public async logout() {
        await firstValueFrom(this.http.post("/api/logout", {}))
        this.currentUser = null
        this.router.navigateByUrl("/login")
    }
}
