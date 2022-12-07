import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpEventType } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { Router } from "@angular/router"
import { map, Observable } from "rxjs"

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(private readonly router: Router) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next.handle(request).pipe(
            map((event: HttpEvent<unknown>) => {
                if (event.type !== HttpEventType.ResponseHeader) {
                    return event
                }

                if (event.status !== 401) {
                    return event
                }

                this.router.navigateByUrl("/login", {
                    replaceUrl: true,

                    state: {
                        intendedUrl: this.router.url,
                    },
                })

                return event
            }),
        )
    }
}
