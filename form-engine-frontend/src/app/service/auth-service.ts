import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {SocialLoginProvider} from '../type/social-login-provider';
import {JwtData, JwtUtil} from '../util/jwt-util';
import {LoginReq} from '../model/auth/login-req';
import {RegisterReq} from '../model/auth/register-req';
import {JwtTokenRes} from '../model/auth/jwt-token-res';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private http = inject(HttpClient)

  getJwt(): string | null {
    return localStorage.getItem('jwt');
  }

  getJwtData(): JwtData | null {
    const jwtToken = localStorage.getItem('jwt');

    return jwtToken === null ? null : JwtUtil.decodeJwt(jwtToken)
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('jwt') !== null && !this.isJwtTokenExpired()
  }

  isJwtTokenExpired(): boolean {
    const jwtToken = localStorage.getItem('jwt');

    if (jwtToken === null) {
      return true;
    }

    const jwtData = JwtUtil.decodeJwt(jwtToken);

    const currTime = Date.now();
    const jwtExpTime = (jwtData.exp ?? 0) * 1000;

    return jwtExpTime < currTime;
  }

  saveJwtToken(jwt: string): void {
    localStorage.setItem('jwt', jwt);
  }

  loginWithSocial(socialLoginProvider: SocialLoginProvider) {
    const url = `oauth2/authorization/${socialLoginProvider}`
    location.assign(url);
  }

  loginWithEmailPassword(credentials: LoginReq, onComplete?: () => void, onError?: (err: HttpErrorResponse) => void) {
    const url = 'auth/login/email-pass'

    this.http.post<JwtTokenRes>(url, credentials).subscribe({
      next: res => {
        this.saveJwtToken(res.jwtToken)
      },
      complete: onComplete,
      error: onError
    })
  }

  registerWithEmailPassword(credentials: RegisterReq, onComplete?: () => void, onError?: (err: HttpErrorResponse) => void) {
    const url = 'auth/register/email-pass'

    this.http.post<JwtTokenRes>(url, credentials).subscribe({
      next: res => {
        this.saveJwtToken(res.jwtToken)
      },
      complete: onComplete,
      error: onError
    })
  }

}
