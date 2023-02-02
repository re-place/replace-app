import { Injectable } from "@angular/core"
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router"
import { Observable } from "rxjs"

import { AuthService } from "../services/auth.service"

@Injectable({
    providedIn: "root",
})
export class AuthGuard implements CanActivate {
    constructor(private readonly router: Router, private readonly auth: AuthService) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot,
    ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        return this.auth.isAuthenticated().then((isAuthenticated: boolean) => {
            if (isAuthenticated) {
                return true
            }

            // this.router.navigateByUrl("/session/login", {
            //     replaceUrl: true,
            //     state: {
            //         intendedUrl: state.url,
            //     },
            // })
            window.location.href = "http://localhost:8000/api/session/login"
            return false
        })
    }
}
