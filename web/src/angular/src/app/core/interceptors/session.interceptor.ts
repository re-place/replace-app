import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpEventType, HttpErrorResponse } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { Observable, tap } from "rxjs"

import { SessionService } from "../services/session.service"

@Injectable()
export class SessionInterceptor implements HttpInterceptor {
    constructor(private readonly sessionService: SessionService) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        const sessionToken = this.sessionService.sessionToken

        if (sessionToken !== null) {
            request = request.clone({
                setHeaders: {
                    SESSION_TOKEN: sessionToken,
                },
            })
        }

        return next.handle(request).pipe(
            tap({
                next: (event: HttpEvent<unknown>) => {
                    if (event.type !== HttpEventType.ResponseHeader) {
                        return event
                    }

                    if (!event.headers.has("SESSION_TOKEN")) {
                        return event
                    }

                    this.sessionService.sessionToken = event.headers.get("SESSION_TOKEN")
                    return event
                },
                error: (error) => {
                    if (!(error instanceof HttpErrorResponse)) {
                        return
                    }

                    this.sessionService.sessionToken = error.headers.get("SESSION_TOKEN")
                },
            }),
        )
    }
}
