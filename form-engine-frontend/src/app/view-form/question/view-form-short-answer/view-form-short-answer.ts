import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {ShortAnswerRes} from '../../../model/edit-form/question/response/short-answer-res';
import {AnyShortAnswerValidationConfig} from '../../../type/any-short-answer-validation-config';
import {FormControl, FormGroup, ReactiveFormsModule, ValidatorFn, Validators} from '@angular/forms';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {ShortAnswerValidationConfig} from '../../../model/validation-config/short-answer-validation-config';
import {NumberValidator} from '../../../formValidator/number-validator';
import {TextValidator} from '../../../formValidator/text-validator';
import {RegexValidator} from '../../../formValidator/regex-validator';
import {OnlyShortAnswerResponsePutReq} from '../../../model/view-form/request/short-answer-response-put-req';

@Component({
  selector: 'app-view-form-short-answer',
  imports: [
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './view-form-short-answer.html',
  styleUrl: './view-form-short-answer.scss',
})
export class ViewFormShortAnswer
  extends ViewFormQuestionComponent<ShortAnswerRes<AnyShortAnswerValidationConfig>, OnlyShortAnswerResponsePutReq> {

  protected formValidator: ValidatorFn | null = null
  protected formError = signal<string | null>(null)

  override formGroup = new FormGroup({
    shortAnswer: new FormControl<string | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.formValidator = this.getFormValidator()
    this.formGroup.controls.shortAnswer.addValidators(this.formValidator ?? [])

    this.formGroup.controls.shortAnswer.valueChanges.subscribe(() => {
      if (this.formValidator) {
        const isInvalid = this.formGroup.controls.shortAnswer.invalid
        this.formError.set(
          isInvalid ? this.question().validationConfig.errorText ?? 'There is some error' : null
        )
      }
    })
  }

  protected override formSubmitCallback = () => {
    this.formGroup.markAllAsTouched()
    const control = this.formGroup.controls.shortAnswer
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

  override getOnlyQuestionResponsePutReq(): OnlyShortAnswerResponsePutReq | null {
    const value = this.formGroup.value.shortAnswer
    return !value ? null : {text: value}
  }

  private getFormValidator(): ValidatorFn | null {
    const validationConfig = this.question().validationConfig
    switch (validationConfig.validationId) {
      case 'SHORT_ANSWER_NUMBER_BETWEEN': {
        const vCon = validationConfig as ShortAnswerValidationConfig.Between
        return NumberValidator.between(vCon.fromNumber, vCon.toNumber)
      }
      case 'SHORT_ANSWER_NUMBER_EQUAL_TO': {
        const vCon = validationConfig as ShortAnswerValidationConfig.EqualTo
        return NumberValidator.equalTo(vCon.number)
      }
      case 'SHORT_ANSWER_NUMBER_GREATER_THAN': {
        const vCon = validationConfig as ShortAnswerValidationConfig.GreaterThan
        return NumberValidator.greaterThan(vCon.number)
      }
      case 'SHORT_ANSWER_NUMBER_IS_NUMBER': {
        return NumberValidator.isNumber
      }
      case 'SHORT_ANSWER_NUMBER_GREATER_THAN_OR_EQUAL_TO': {
        const vCon = validationConfig as ShortAnswerValidationConfig.GreaterThanOrEqualTo
        return NumberValidator.greaterThanOrEqualTo(vCon.number)
      }
      case 'SHORT_ANSWER_NUMBER_LESS_THAN': {
        const vCon = validationConfig as ShortAnswerValidationConfig.LessThan
        return NumberValidator.lessThan(vCon.number)
      }
      case 'SHORT_ANSWER_NUMBER_LESS_THAN_OR_EQUAL_TO': {
        const vCon = validationConfig as ShortAnswerValidationConfig.LessThanOrEqualTo
        return NumberValidator.lessThanOrEqualTo(vCon.number)
      }
      case 'SHORT_ANSWER_NUMBER_NOT_BETWEEN': {
        const vCon = validationConfig as ShortAnswerValidationConfig.NotBetween
        return NumberValidator.notBetween(vCon.fromNumber, vCon.toNumber)
      }
      case 'SHORT_ANSWER_NUMBER_NOT_EQUAL_TO': {
        const vCon = validationConfig as ShortAnswerValidationConfig.NotEqualTo
        return NumberValidator.notEqualTo(vCon.number)
      }
      case 'SHORT_ANSWER_NUMBER_WHOLE_NUMBER': {
        return NumberValidator.wholeNumber
      }
      case 'SHORT_ANSWER_TEXT_CONTAINS': {
        const vCon = validationConfig as ShortAnswerValidationConfig.Contains
        return TextValidator.contains(vCon.text)
      }
      case 'SHORT_ANSWER_TEXT_EMAIL': {
        return Validators.email
      }
      case 'SHORT_ANSWER_TEXT_NOT_CONTAINS': {
        const vCon = validationConfig as ShortAnswerValidationConfig.DoesNotContains
        return TextValidator.doesNotContains(vCon.text)
      }
      case 'SHORT_ANSWER_TEXT_URL': {
        return TextValidator.url
      }
      case 'SHORT_ANSWER_LENGTH_MAX_CHARACTER_COUNT': {
        const vCon = validationConfig as ShortAnswerValidationConfig.MaxCharacterCount
        return Validators.maxLength(vCon.number)
      }
      case 'SHORT_ANSWER_LENGTH_MIN_CHARACTER_COUNT': {
        const vCon = validationConfig as ShortAnswerValidationConfig.MinCharacterCount
        return Validators.minLength(vCon.number)
      }
      case 'SHORT_ANSWER_REGEX_MATCHES': {
        const vCon = validationConfig as ShortAnswerValidationConfig.Matches
        return RegexValidator.matches(vCon.text)
      }
      case 'SHORT_ANSWER_REGEX_DOES_NOT_MATCHES': {
        const vCon = validationConfig as ShortAnswerValidationConfig.DoesNotMatch
        return RegexValidator.doesNotMatches(vCon.text)
      }
      default:
        return null
    }
  }
}
