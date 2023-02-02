import {HttpClient} from "@angular/common/http"
import {Injectable} from "@angular/core"
import {Router} from "@angular/router"
import {firstValueFrom} from "rxjs"

import {UserDto} from "../openapi"

@Injectable({
    providedIn: "root",
})
export class AuthService {
    currentUser: UserDto | null = null
    loginError: string | undefined = undefined
    private readonly intendedUrl: string | null = null

    constructor(private readonly http: HttpClient, private readonly router: Router) {
        const intendedUrl = this.router.getCurrentNavigation()?.extras.state?.["intendedUrl"]
        if (typeof intendedUrl === "string") {
            this.intendedUrl = intendedUrl
        }
    }

    public login(username: string, password: string) {
        const req = this.http.post<UserDto>("/api/login", {username, password}, {observe: "response"})
        req.subscribe({
            next: res => {
                // Save user object
                this.currentUser = res.body

                let url = "/"
                if (this.intendedUrl !== null) {
                    url = this.intendedUrl
                }
                this.router.navigateByUrl(url)
            },
            error: err => {
                console.log(err)
                this.loginError = err.error
            },
        })
    }

    public getLoginError(): string {
        return this.loginError ?? ""
    }

    public async isAuthenticated(): Promise<boolean> {
        if (this.currentUser !== null) {
            return true
        }

        try {
            this.currentUser = await firstValueFrom(this.http.get<UserDto>("/api/session/current-user"))
            return true
        } catch (error) {
            return false
        }
    }

    public async logout() {
        await firstValueFrom(this.http.post("/api/session/logout", {}))
        this.currentUser = null
    }
}
