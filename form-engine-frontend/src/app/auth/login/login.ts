import {Component, inject, signal} from '@angular/core';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatCard} from '@angular/material/card';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {MatButton, MatIconButton} from '@angular/material/button';
import {AuthService} from '../../service/auth-service';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {SocialLoginProvider} from '../../type/social-login-provider';
import {SimpleDialog} from '../../shared/simple-dialog/simple-dialog';

@Component({
  selector: 'app-login',
  imports: [
    MatFormField,
    MatLabel,
    MatCard,
    MatInput,
    MatError,
    ReactiveFormsModule,
    MatIcon,
    MatButton,
    MatIconButton
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

  private authService = inject(AuthService)
  private dialog = inject(MatDialog)
  private router = inject(Router)

  protected hidePassword = signal(true);
  protected disableLoginButton = signal<boolean>(false)

  protected formGroup = new FormGroup({
    email: new FormControl<string>('', [Validators.required, Validators.email]),
    password: new FormControl<string>('', [Validators.required])
  })

  protected onSocialLoginClick(socialLoginProvider: SocialLoginProvider) {
    this.authService.loginWithSocial(socialLoginProvider);
  }

  protected loginUserWithEmailPassword(event: SubmitEvent) {
    this.formGroup.markAllAsTouched()

    if (this.formGroup.invalid) return

    this.disableLoginButton.set(true)

    this.authService.loginWithEmailPassword({
      email: this.formGroup.value.email!,
      password: this.formGroup.value.password!
    }, () => {
      this.disableLoginButton.set(false)
      this.router.navigate(['home'])
    }, err => {
      this.disableLoginButton.set(false)
      this.dialog.open(SimpleDialog, {
        data: SimpleDialog.configure('Something went wrong', err.error.message, 'Ok')
      })
    })
  }

}
