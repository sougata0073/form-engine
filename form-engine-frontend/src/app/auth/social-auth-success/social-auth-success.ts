import {Component, inject, input, OnInit} from '@angular/core';
import {AuthService} from '../../service/auth-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-social-auth-success',
  imports: [],
  templateUrl: './social-auth-success.html',
  styleUrl: './social-auth-success.scss',
})
export class SocialAuthSuccess implements OnInit {

  jwt = input<string>('jwt');

  private authService = inject(AuthService);
  private router = inject(Router);

  ngOnInit() {
    this.authService.saveJwtToken(this.jwt())

    if (this.authService.isLoggedIn()) {
      this.router.navigate(['home'])
    }
  }

}
