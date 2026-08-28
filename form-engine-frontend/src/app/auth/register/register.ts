import {Component, inject, OnInit, signal} from '@angular/core';
import {MatCard} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatInputModule, MatLabel} from '@angular/material/input';
import {MatButton, MatButtonModule, MatIconButton} from '@angular/material/button';
import {AuthService} from '../../service/auth-service';
import {SocialLoginProvider} from '../../type/social-login-provider';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {FormGroupValidator} from '../../formValidator/form-group-validator';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDialog} from '@angular/material/dialog';
import {SimpleDialog} from '../../shared/simple-dialog/simple-dialog';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [
    MatCard,
    MatFormField,
    MatLabel,
    MatButton,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    MatError,
    MatIconButton,
    MatIcon,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register implements OnInit {

  private authService = inject(AuthService)
  private dialog = inject(MatDialog)
  private router = inject(Router)

  protected hidePassword = signal(true);
  protected hideConfirmPassword = signal(true);
  protected disableRegisterButton = signal<boolean>(false)

  protected formGroup = new FormGroup({
    userName: new FormControl<string>('', [Validators.required]),
    email: new FormControl<string>('', [Validators.required, Validators.email]),
    password: new FormControl<string>('', [Validators.required]),
    confirmPassword: new FormControl<string>('')
  })

  ngOnInit() {
    this.formGroup.addValidators([
      FormGroupValidator.bothFieldSameValidator(this.formGroup.controls.password, this.formGroup.controls.confirmPassword)
    ])

    this.formGroup.valueChanges.subscribe(val => {
      if (this.formGroup.hasError('bothFieldSame')) {
        this.formGroup.controls.confirmPassword.setErrors({bothFieldSame: true})
      }
    })
  }

  protected onSocialLoginClick(socialLoginProvider: SocialLoginProvider) {
    this.authService.loginWithSocial(socialLoginProvider);
  }

  protected onLoginClick() {
    this.router.navigate(['login'])
  }

  protected registerUserWithEmailPassword(event: SubmitEvent) {
    this.formGroup.markAllAsTouched()
    this.formGroup.controls.confirmPassword.markAsDirty()

    if (this.formGroup.invalid) return

    this.disableRegisterButton.set(true)

    this.authService.registerWithEmailPassword({
      username: this.formGroup.value.userName!,
      email: this.formGroup.value.email!,
      password: this.formGroup.value.password!,
      avatarUrl: null
    }, () => {
      this.disableRegisterButton.set(false)
      this.router.navigate(['home'])
    }, err => {
      this.disableRegisterButton.set(false)
      this.dialog.open(SimpleDialog, {
        data: SimpleDialog.configure('Something went wrong', err.error.message, 'Ok')
      })
    })

  }
}
