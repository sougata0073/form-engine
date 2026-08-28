import {Component, inject, input, OnDestroy, OnInit, signal} from '@angular/core';
import {ViewFormInfo} from './view-form-info/view-form-info';
import {ViewFormService} from '../service/view-form-service';
import {ViewFormQuestionWrapper} from './view-form-question-wrapper/view-form-question-wrapper';
import {MatButton} from '@angular/material/button';
import {ViewFormStateService} from '../service/view-form-state-service';
import {SimpleDialog} from '../shared/simple-dialog/simple-dialog';
import {MatDialog} from '@angular/material/dialog';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {Title} from '@angular/platform-browser';
import {QuestionCard} from '../shared/question-card/question-card';
import {ViewFormErrorRes} from '../model/form/view-form-error-res';
import {NgOptimizedImage} from '@angular/common';
import {BehaviorSubject} from 'rxjs';
import {FormResponsePutReq} from '../model/form/form-response-put-req';
import {AnyQuestionResponsePutReq} from '../type/any-question-response-put-req';
import {FunctionalDialog} from '../shared/functional-dialog/functional-dialog';
import {ViewFormFooter} from '../shared/view-form-footer/view-form-footer';

@Component({
  selector: 'app-view-form',
  imports: [
    ViewFormInfo,
    ViewFormQuestionWrapper,
    MatButton,
    MatProgressSpinner,
    QuestionCard,
    NgOptimizedImage,
    ViewFormFooter
  ],
  templateUrl: './view-form.html',
  styleUrl: './view-form.scss',
})
export class ViewForm implements OnInit, OnDestroy {

  formId = input.required<string>()

  protected formError = signal<ViewFormErrorRes | null>(null)
  protected questionResponses = signal<AnyQuestionResponsePutReq[]>([])

  protected _submitClick = new BehaviorSubject<boolean>(false)
  protected submitClick = this._submitClick.asObservable()
  protected _clearClick = new BehaviorSubject<boolean>(false)
  protected clearClick = this._clearClick.asObservable()

  protected viewFormService = inject(ViewFormService)
  protected viewFormStateService = inject(ViewFormStateService)
  protected dialog = inject(MatDialog)
  protected title = inject(Title)

  protected formRes = this.viewFormService.formRes

  protected isFormSubmitInProgress = signal<boolean>(false)
  protected isFormSubmitted = signal<boolean>(false)

  ngOnInit() {
    this.viewFormService.loadFormRes(this.formId(), res => {

      this.title.setTitle(res.title ?? 'Form engine')

    }, error => this.formError.set(error))

  }

  ngOnDestroy() {
    this.viewFormStateService.removeAllFormSubmitCallback()
  }

  protected onQuestionResponseEmit(req: AnyQuestionResponsePutReq) {
    this.questionResponses.update(prev => [...prev, req])
  }

  protected onFormSubmit(submitBtn: MatButton) {
    this.isFormSubmitInProgress.set(true)

    this.viewFormStateService.executeAllFormSubmitCallbacks()
    const hasError = this.viewFormStateService.ifAnyQuestionHasError()

    if (hasError) {
      this.dialog.open(
        SimpleDialog, {
          data: SimpleDialog.configure(
            'Error',
            'Please review the highlighted fields and correct the errors before submitting',
            'Ok'
          )
        }
      )

      this.isFormSubmitInProgress.set(false)

      return
    }

    submitBtn.disabled = true

    this.questionResponses.set([])
    this._submitClick.next(true)

    const req: FormResponsePutReq = {
      responses: this.questionResponses()
    }

    this.viewFormService.submitResponse(this.formId(), req, () => {
      this.isFormSubmitted.set(true)
      this.isFormSubmitInProgress.set(false)
    }, () => {
      this.isFormSubmitted.set(false)
      this.isFormSubmitInProgress.set(false)
      submitBtn.disabled = false
    })

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
          this._clearClick.next(true)
          dialogRef.close()
        }
      )
    })
  }

}
