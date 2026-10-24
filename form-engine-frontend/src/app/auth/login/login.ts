import { Component, OnInit } from '@angular/core';
import { CommonConstant } from '../../constant/common-contant';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login implements OnInit {
  ngOnInit(): void {
    location.assign(`${CommonConstant.SERVER_URL}oauth2/authorization/google`);
  }
}
