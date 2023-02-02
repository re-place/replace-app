import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpEventType, HttpErrorResponse } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { Router } from "@angular/router"
import { Observable, tap } from "rxjs"

import { AuthService } from "../services/auth.service"

@Injectable()
export class NotAuthenticatedRedirect implements HttpInterceptor {
    constructor(private readonly authService: AuthService, private readonly router: Router) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next.handle(request).pipe(
            tap({
                next: (event: HttpEvent<unknown>) => {
                    if (event.type !== HttpEventType.ResponseHeader) {
                        return event
                    }

                    if (event.status !== 401) {
                        return event
                    }

                    this.authService.currentUser = null
                    window.location.href = "http://localhost:8000/api/session/login"
                    // this.router.navigateByUrl("http://localhost:8000/api/session/login")

                    return event
                },
                error: (error) => {
                    if (!(error instanceof HttpErrorResponse)) {
                        return
                    }

                    if (error.status !== 401) {
                        return
                    }

                    this.authService.currentUser = null
                    window.location.href = "http://localhost:8000/api/session/login"

                    // this.router.navigateByUrl("/session/login")
                },
            }),
        )
    }
}
