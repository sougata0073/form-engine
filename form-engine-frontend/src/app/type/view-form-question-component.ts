import {Directive, inject, input, OnInit, output, signal} from '@angular/core';
import {QuestionRes} from '../model/edit-form/question/response/question-res';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {ViewFormStateService} from '../service/view-form-state-service';

@Directive()
export abstract class ViewFormQuestionComponent<QR extends QuestionRes, OnlyQuestionResponsePutReq> implements OnInit {

  question = input.required<QR>();
  parentComponentId = input.required<string>()

  hasError = output<boolean>()

  showError = signal<boolean>(false)

  abstract formGroup: FormGroup

  protected viewFormStateService = inject(ViewFormStateService)

  abstract getOnlyQuestionResponsePutReq(): OnlyQuestionResponsePutReq | null

  clearForm() {
    Object.values(this.formGroup.controls).forEach(control => {
      control.patchValue(null)
    })
  }

  ngOnInit() {
    if (this.question().required) {
      Object.values(this.formGroup.controls).forEach(control => {
        control.addValidators(Validators.required)
      })
    }

    this.viewFormStateService.addFormSubmitCallback(this.formSubmitCallback)

    this.formGroup.valueChanges.subscribe(() => {
      this.formGroup.markAllAsTouched()
      this.showError.set(this.formGroup.invalid)
      this.hasError.emit(this.formGroup.invalid)
    })
  }

  protected formSubmitCallback = () => {
    this.viewFormStateService.putToQuestionErrorMap(
      this.question().id, this.formGroup.invalid
    )
    this.formGroup.markAllAsTouched()
    this.showError.set(this.formGroup.invalid)
    this.hasError.emit(this.formGroup.invalid)
  }

  protected onFocusOut(formControl: FormControl) {
    this.showError.set(formControl.invalid)
    this.hasError.emit(formControl.invalid)
  }

  protected formControlError(
    control: FormControl | FormArray, errors: { errorCode: string, message: string }[]
  ): string | null {
    if (!control.touched) return null

    let errorMessage: string | null = null

    errors.forEach(error => {
      if (control.hasError(error.errorCode)) {
        errorMessage = error.message
        return
      }
    })

    if (control.hasError('required') && errorMessage === null) {
      errorMessage = 'This is a required question'
    }

    return errorMessage
  }
}
