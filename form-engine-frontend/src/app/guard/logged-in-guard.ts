import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from '../service/auth-service';
import {inject} from '@angular/core';

export const loggedInGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  return router.createUrlTree(['register']);
};
