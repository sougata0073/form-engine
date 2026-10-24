import { Component, inject, input, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-social-auth-success',
  imports: [],
  templateUrl: './auth-success.html',
  styleUrl: './auth-success.scss',
})
export class SocialAuthSuccess implements OnInit {
  jwt = input.required<string>();

  private authService = inject(AuthService);
  private router = inject(Router);

  ngOnInit() {
    this.authService.saveJwt(this.jwt());

    if (this.authService.isLoggedIn()) {
      this.router.navigate(['home']);
    }
  }
}
