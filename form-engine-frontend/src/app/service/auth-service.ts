import {Injectable} from '@angular/core';
import {JwtData, JwtUtil} from '../util/jwt-util';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  getJwt(): string | null {
    return localStorage.getItem('jwt');
  }

  getJwtData(): JwtData | null {
    const jwtToken = localStorage.getItem('jwt');

    return jwtToken === null ? null : JwtUtil.decodeJwt(jwtToken)
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('jwt') !== null && !this.isJwtExpired()
  }

  isJwtExpired(): boolean {
    const jwtToken = localStorage.getItem('jwt');

    if (jwtToken === null) {
      return true;
    }

    const jwtData = JwtUtil.decodeJwt(jwtToken);

    const currTime = Date.now();
    const jwtExpTime = (jwtData.exp ?? 0) * 1000;

    return jwtExpTime < currTime;
  }

  saveJwt(jwt: string): void {
    localStorage.setItem('jwt', jwt);
  }

}
