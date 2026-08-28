import {jwtDecode, JwtPayload} from 'jwt-decode';

export interface JwtData extends JwtPayload {
  name: string;
  email: string;
  avatarUrl: string;
}

export class JwtUtil {
  static decodeJwt(jwt: string): JwtData {
    return jwtDecode<JwtData>(jwt);
  }
}
