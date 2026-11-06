import { Component, OnInit } from '@angular/core';
import { CommonConstant } from '../../constant/common-contant';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login implements OnInit {
  ngOnInit(): void {
    location.assign(AuthService.GOOGLE_AUTH_URL);
  }
}
