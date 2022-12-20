import { HttpClient } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { Router } from "@angular/router"
import { firstValueFrom } from "rxjs"
import { User } from "types"
import { DefaultService } from "../openapi"

@Injectable({
    providedIn: "root",
})
export class AuthService {
    currentUser: User | null = null
    loginError: string | undefined = undefined
    private readonly intendedUrl: string | null = null

    constructor(private readonly http: HttpClient, private readonly router: Router, private apiService: DefaultService) {
        const intendedUrl = this.router.getCurrentNavigation()?.extras.state?.["intendedUrl"]
        if(typeof intendedUrl === "string") {
            this.intendedUrl = intendedUrl
        }
    }

    public login(username: string, password: string) {
        const req = this.http.post<User>("/api/login", {username, password}, {observe: "response"})
        req.subscribe({
            next: res => {
                // Append session token to the headers (not happy about this,
                // should be a cookie, not a header)
                this.addSessionTokenHeader(res.headers.get("SESSION_TOKEN"))

                // Save user object
                this.currentUser = res.body

                let url = "/"
                if(this.intendedUrl !== null) {
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
        return this.loginError?? ""
    }

    private addSessionTokenHeader(token: string | null) {
        if(!token) return
        this.apiService.defaultHeaders = this.apiService.defaultHeaders.append("SESSION_TOKEN", token)
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
