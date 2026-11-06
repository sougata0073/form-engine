import {
  Component,
  inject,
  input,
  OnChanges,
  OnInit,
  output,
  signal,
  SimpleChanges,
} from '@angular/core';
import { QuestionCard } from '../../shared/question-card/question-card';
import { FormDetails } from '../../model/form/form-details';
import { AuthService } from '../../service/auth-service';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatError, MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { ViewFormStateService } from '../../service/view-form-state-service';

@Component({
  selector: 'app-view-form-collect-email',
  imports: [
    QuestionCard,
    MatCheckbox,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
  ],
  templateUrl: './view-form-collect-email.html',
  styleUrl: './view-form-collect-email.scss',
})
export class ViewFormCollectEmail implements OnInit, OnChanges {
  formRes = input.required<FormDetails>();

  private questionId = crypto.randomUUID();

  protected authService = inject(AuthService);
  private viewFormStateService = inject(ViewFormStateService);

  hasError = signal<boolean>(false);

  protected formGroup = new FormGroup({
    email: new FormControl<string | null>(null, {
      nonNullable: true,
      validators: [Validators.email],
    }),
    verifiedEmailConfirmation: new FormControl<boolean | null>(null, {
      nonNullable: true,
    }),
  });

  ngOnInit(): void {
    this.initialize();

    this.formGroup.valueChanges.subscribe((val) => {
      this.hasError.set(this.formGroup.invalid);
    });

    this.viewFormStateService.setFormSubmitCallback(this.questionId, this.formSubmitCallback);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['formRes']) {
      this.initialize();
      this.viewFormStateService.setFormSubmitCallback(this.questionId, this.formSubmitCallback);
    }
  }

  private initialize() {
    const emailCollectionOption = this.formRes().formInfo.responseSettings.emailCollectionOption;

    if (emailCollectionOption === 'RESPONDER_INPUT') {
      this.formGroup.controls.email.addValidators(Validators.required);
    } else if (emailCollectionOption === 'VERIFIED') {
      this.formGroup.controls.verifiedEmailConfirmation.addValidators(Validators.required);
    }
  }

  protected formSubmitCallback = () => {
    this.viewFormStateService.putToQuestionErrorMap(this.questionId, this.formGroup.invalid);
    this.formGroup.markAllAsTouched();
    this.hasError.set(this.formGroup.invalid);
  };
}
