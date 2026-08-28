import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../service/auth-service';

export class HttpInterceptors {

  static baseUrlInterceptor(baseUrl: string): HttpInterceptorFn {
    return (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
      const newReq = req.clone({
        url: baseUrl + req.url
      })

      return next(newReq)
    }
  }

  public static AuthJwtInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
    const authService = inject(AuthService)

    if (!authService.isLoggedIn()) {
      return next(req)
    }

    const newReq = req.clone({
      headers: req.headers.append('auth-jwt', authService.getJwtData()?.sub!)
    })

    return next(newReq)
  }

}
