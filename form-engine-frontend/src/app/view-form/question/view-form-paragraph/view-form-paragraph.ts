import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {ParagraphRes} from '../../../model/edit-form/question/response/paragraph-res';
import {AnyParagraphValidationConfig} from '../../../type/any-paragraph-validation-config';
import {FormControl, FormGroup, ReactiveFormsModule, ValidatorFn, Validators} from '@angular/forms';
import {MatError, MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {ParagraphValidationConfig} from '../../../model/validation-config/paragraph-validation-config';
import {RegexValidator} from '../../../formValidator/regex-validator';
import {OnlyParagraphResponsePutReq} from '../../../model/view-form/request/paragraph-response-put-req';
import {CdkTextareaAutosize} from '@angular/cdk/text-field';

@Component({
  selector: 'app-view-form-paragraph',
  imports: [
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    CdkTextareaAutosize
  ],
  templateUrl: './view-form-paragraph.html',
  styleUrl: './view-form-paragraph.scss',
})
export class ViewFormParagraph
  extends ViewFormQuestionComponent<ParagraphRes<AnyParagraphValidationConfig>, OnlyParagraphResponsePutReq> {

  protected formValidator: ValidatorFn | null = null
  protected formError = signal<string | null>(null)

  override formGroup = new FormGroup({
    paragraph: new FormControl<string | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.formValidator = this.getFormValidator()
    this.formGroup.controls.paragraph.addValidators(this.formValidator ?? [])

    this.formGroup.controls.paragraph.valueChanges.subscribe(() => {
      if (this.formValidator) {
        const isInvalid = this.formGroup.controls.paragraph.invalid
        this.formError.set(
          isInvalid ? this.question().validationConfig.errorText ?? 'There is some error' : null
        )
      }
    })
  }

  protected override formSubmitCallback = () => {
    this.formGroup.markAllAsTouched()
    const control = this.formGroup.controls.paragraph
    const isInvalid =
      (this.formValidator && control.value !== null && control.invalid) ||
      control.hasError('required')

    this.viewFormStateService.putToQuestionErrorMap(
      this.question().id, isInvalid
    )
    this.showError.set(isInvalid)
    this.hasError.emit(isInvalid)
  }

  protected override onFocusOut(formControl: FormControl) {
    if (this.formValidator) {
      const isInvalid = formControl.value !== null && formControl.invalid
      this.formError.set(
        isInvalid ? this.question().validationConfig.errorText ?? 'There is some error' : null
      )
      this.hasError.emit(isInvalid)
    }
    if (formControl.hasError('required')) {
      this.formError.set('This is a required question')
      this.hasError.emit(true)
    }
  }

  override getOnlyQuestionResponsePutReq(): OnlyParagraphResponsePutReq | null {
    const value = this.formGroup.value.paragraph
    return !value ? null : {text: value}
  }

  private getFormValidator(): ValidatorFn | null {
    const validationConfig = this.question().validationConfig
    switch (validationConfig.validationId) {
      case 'PARAGRAPH_LENGTH_MIN_CHARACTER_COUNT': {
        const vCon = validationConfig as ParagraphValidationConfig.MinCharacterCount
        return Validators.minLength(vCon.number)
      }
      case 'PARAGRAPH_LENGTH_MAX_CHARACTER_COUNT': {
        const vCon = validationConfig as ParagraphValidationConfig.MaxCharacterCount
        return Validators.maxLength(vCon.number)
      }
      case 'PARAGRAPH_REGEX_MATCHES': {
        const vCon = validationConfig as ParagraphValidationConfig.Matches
        return RegexValidator.matches(vCon.text)
      }
      case 'PARAGRAPH_REGEX_DOES_NOT_MATCHES': {
        const vCon = validationConfig as ParagraphValidationConfig.DoesNotMatch
        return RegexValidator.doesNotMatches(vCon.text)
      }
      default:
        return null
    }
  }

}
