import { Component, inject, input, OnDestroy, OnInit, signal } from '@angular/core';
import { ViewFormInfo } from './view-form-info/view-form-info';
import { ViewFormService } from '../service/view-form-service';
import { ViewFormQuestionWrapper } from './view-form-question-wrapper/view-form-question-wrapper';
import { MatButton } from '@angular/material/button';
import { ViewFormStateService } from '../service/view-form-state-service';
import { SimpleDialog } from '../shared/simple-dialog/simple-dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { Title } from '@angular/platform-browser';
import { QuestionCard } from '../shared/question-card/question-card';
import { ViewFormErrorRes } from '../model/form/view-form-error-res';
import { NgOptimizedImage } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import { FormResponsePutReq } from '../model/form/form-response-put-req';
import { AnyQuestionResponsePutReq } from '../type/any-question-response-put-req';
import { FunctionalDialog } from '../shared/functional-dialog/functional-dialog';
import { ViewFormFooter } from '../shared/view-form-footer/view-form-footer';
import { FormDetails } from '../model/form/form-details';
import { AuthService } from '../service/auth-service';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ɵInternalFormsSharedModule,
} from '@angular/forms';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { ViewFormCollectEmail } from './view-form-collect-email/view-form-collect-email';
import { ConfirmationDialog } from '../shared/confirmation-dialog/confirmation-dialog';

@Component({
  selector: 'app-view-form',
  imports: [
    ViewFormInfo,
    ViewFormQuestionWrapper,
    MatButton,
    MatProgressSpinner,
    QuestionCard,
    NgOptimizedImage,
    ViewFormFooter,
    MatSlideToggle,
    ɵInternalFormsSharedModule,
    ReactiveFormsModule,
    MatProgressBarModule,
    ViewFormCollectEmail,
  ],
  templateUrl: './view-form.html',
  styleUrl: './view-form.scss',
})
export class ViewForm implements OnInit, OnDestroy {
  formId = input.required<string>();

  protected formError = signal<ViewFormErrorRes | null>(null);
  protected questionResponses = signal<AnyQuestionResponsePutReq[]>([]);

  private _submitClick = new BehaviorSubject<boolean>(false);
  protected submitClick = this._submitClick.asObservable();
  private _clearClick = new BehaviorSubject<boolean>(false);
  protected clearClick = this._clearClick.asObservable();

  protected viewFormService = inject(ViewFormService);
  protected viewFormStateService = inject(ViewFormStateService);
  protected authService = inject(AuthService);
  protected dialog = inject(MatDialog);
  protected title = inject(Title);

  protected formRes = signal<FormDetails | null>(null);

  protected isFormSubmitInProgress = signal<boolean>(false);
  protected isFormSubmitted = signal<boolean>(false);
  protected formProgress = signal(100);

  protected formGroup = new FormGroup({
    sendResponseCopy: new FormControl<boolean | null>(null, { nonNullable: true }),
  });

  ngOnInit() {
    this.viewFormService.loadFormRes(
      this.formId(),
      (res) => {
        this.formRes.set(res);
        this.title.setTitle(res.formInfo.title ?? 'Form engine');

        if (
          res.formInfo.responseSettings.emailCollectionOption !== 'OFF' &&
          !this.authService.isLoggedIn()
        ) {
          this.dialog.open(ConfirmationDialog, {
            disableClose: true,
            data: ConfirmationDialog.configure(
              'Sign in to continue',
              'To fill out this form, you must be signed in.',
              'SIGN IN',
              () => {
                location.assign(AuthService.GOOGLE_AUTH_URL);
              },
            ),
          });
        }
      },
      (error) => this.formError.set(error),
    );
  }

  ngOnDestroy() {
    this.viewFormStateService.removeAllFormSubmitCallback();
  }

  protected onQuestionResponseEmit(req: AnyQuestionResponsePutReq) {
    this.questionResponses.update((prev) => [...prev, req]);
  }

  protected onFormSubmit(submitBtn: MatButton) {
    this.isFormSubmitInProgress.set(true);

    this.viewFormStateService.executeAllFormSubmitCallbacks();
    const hasError = this.viewFormStateService.ifAnyQuestionHasError();

    if (hasError) {
      this.dialog.open(SimpleDialog, {
        data: SimpleDialog.configure(
          'Error',
          'Please review the highlighted fields and correct the errors before submitting',
          'Ok',
        ),
      });

      this.isFormSubmitInProgress.set(false);

      this.viewFormService.loadFormRes(
        this.formId(),
        (res) => {
          this.viewFormStateService.removeAllFormSubmitCallback();

          this.formRes.set(res);

          this.title.setTitle(res.formInfo.title ?? 'Form engine');
        },
        (error) => this.formError.set(error),
      );

      return;
    }

    submitBtn.disabled = true;

    this.questionResponses.set([]);
    this._submitClick.next(true);

    const emailCollectionOption = this.formRes()?.formInfo.responseSettings?.emailCollectionOption;

    const email = emailCollectionOption === 'OFF' ? null : this.authService.getJwtData()!.email;

    const req: FormResponsePutReq = {
      formDetailsResponseSubmittedOn: this.formRes()!,
      responses: this.questionResponses(),
      sendResponseCopy: this.formGroup.getRawValue().sendResponseCopy,
      email: email,
    };

    this.viewFormService.submitResponse(
      this.formId(),
      req,
      () => {
        this.isFormSubmitted.set(true);
        this.isFormSubmitInProgress.set(false);
      },
      (problemDetail) => {
        this.isFormSubmitted.set(false);
        this.isFormSubmitInProgress.set(false);
        submitBtn.disabled = false;

        if (problemDetail.title === 'Form details may changed') {
          this.viewFormService.loadFormRes(
            this.formId(),
            (res) => {
              this.viewFormStateService.removeAllFormSubmitCallback();

              this.formRes.set(res);

              this.title.setTitle(res.formInfo.title ?? 'Form engine');
            },
            (error) => this.formError.set(error),
          );
        } else if (problemDetail.title === 'Form can not be submitted') {
          this.formError.set({
            formTitle: this.formRes()?.formInfo.title ?? null,
            message: problemDetail.detail,
            reason: 'RESPONSE_ALREADY_SUBMITTED',
          });
        }
      },
    );
  }

  protected onClearClick() {
    const dialogRef = this.dialog.open(FunctionalDialog, {
      data: FunctionalDialog.configure(
        'Clear form?',
        'This will remove your answers from all questions and cannot be undone.',
        'Cancel',
        () => dialogRef.close(),
        'Clear form',
        () => {
          this._clearClick.next(true);
          dialogRef.close();
        },
      ),
    });
  }

  protected onSubmitAnotherResponseClick() {
    const url = `${window.origin}/forms/${this.formId()}/view`;
    window.open(url, '_blank');
  }
}
