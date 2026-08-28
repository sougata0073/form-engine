import {ApplicationConfig, provideBrowserGlobalErrorListeners} from '@angular/core';
import {provideRouter, withComponentInputBinding} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {HttpInterceptors} from './interceptor/http-interceptors';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(
      withInterceptors(
        [HttpInterceptors.AuthJwtInterceptor]
      )
    )
  ]
};
