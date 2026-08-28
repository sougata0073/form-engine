import {Component, inject, OnChanges, OnInit, signal, SimpleChanges} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatError, MatFormField, MatInput, MatInputModule, MatLabel} from '@angular/material/input';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {ShortAnswerRes} from '../../../../model/edit-form/question/response/short-answer-res';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {ShortAnswerActiveValidationInputId, ShortAnswerConstant} from '../../../../constant/short-answer-constant';
import {ValidationId} from '../../../../type/validation-id';
import {AnyShortAnswerValidationConfig} from '../../../../type/any-short-answer-validation-config';
import {FormGroupValidator} from '../../../../formValidator/form-group-validator';
import {MatFormFieldModule} from '@angular/material/form-field';
import {RegexValidator} from '../../../../formValidator/regex-validator';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {QuestionType} from '../../../../type/question-type';
import {OnlyShortAnswerAddUpdateReq} from '../../../../model/edit-form/question/request/short-answer-add-update-req';

@Component({
  selector: 'app-edit-form-short-answer',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatOption,
    MatSelect,
    MatError,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './edit-form-short-answer.html',
  styleUrl: './edit-form-short-answer.scss',
})
export class EditFormShortAnswer
  extends EditFormQuestionComponent<ShortAnswerRes<AnyShortAnswerValidationConfig>, OnlyShortAnswerAddUpdateReq<AnyShortAnswerValidationConfig>>
  implements OnInit, OnChanges {

  private constant = new ShortAnswerConstant()

  protected validationInputTypes = signal(this.constant.getInputs())
  protected validationValidationTypes = signal(this.constant.getValidationTypes(this.constant.DEFAULT_VALIDATION_ID_META.input.value))
  protected activeValidationInputId = signal<ShortAnswerActiveValidationInputId>(this.constant.DEFAULT_VALIDATION_ID_META.activeValidationInputId)

  protected validationSelectorFg = new FormGroup({
    inputType: new FormControl<string>(this.constant.DEFAULT_VALIDATION_ID_META.input.value, [Validators.required]),
    validationType: new FormControl<string>(this.constant.DEFAULT_VALIDATION_ID_META.validation.value, [Validators.required])
  })

  protected validationValueFg = new FormGroup({
    errorText: new FormControl<string | null>(null),
    number: new FormControl<number | null>(null, [Validators.required]),
    fromNumber: new FormControl<number | null>(null, [Validators.required]),
    toNumber: new FormControl<number | null>(null, [Validators.required]),
    text: new FormControl<string | null>(null, [Validators.required, RegexValidator.pattern])
  }, {
    validators: [FormGroupValidator.rangeInputs('fromNumber', 'toNumber')]
  })

  protected formStateService = inject(EditFormStateService)

  ngOnInit() {
    this.setupResponseValidationForm()

    this.emitCanSaveAndHasError()

    this.validationSelectorFg.valueChanges.subscribe(() => {
      const validationId = this.getValidationId()
      this.activeValidationInputId.set(this.constant.getByValidationId(validationId).activeValidationInputId)
      this.validationValueFg.reset()
      this.validationValueFg.markAsUntouched()
      this.emitCanSaveAndHasError()
      this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
    })
    this.validationSelectorFg.controls.inputType.valueChanges.subscribe(val => {
      if (!val) return

      this.validationValidationTypes.set(this.constant.getValidationTypes(val))
      this.validationSelectorFg.controls.validationType.patchValue(this.validationValidationTypes()[0].value)
    })

    this.validationValueFg.valueChanges.subscribe(() => {
      this.emitCanSaveAndHasError()
      this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    const moreMenuItemIdsChange = changes['moreMenuItemIds']
    if (moreMenuItemIdsChange) {

      if (
        !moreMenuItemIdsChange.isFirstChange() &&
        !this.moreMenuItemIds().has('responseValidation')
      ) {
        this.validationSelectorFg.patchValue({
          inputType: this.constant.DEFAULT_VALIDATION_ID_META.input.value,
          validationType: this.constant.DEFAULT_VALIDATION_ID_META.validation.value
        })
        this.validationValueFg.reset()
        this.validationValueFg.markAllAsTouched()
      }

      this.emitCanSaveAndHasError()
    }
  }

  override getOnlyQuestionAddUpdateReq(): OnlyShortAnswerAddUpdateReq<AnyShortAnswerValidationConfig> {
    return {
      validationConfig: this.getValidationConfig()
    }
  }

  protected showResponseValidation(): boolean {
    return this.moreMenuItemIds().has('responseValidation') && this.formStateService.isFocused(this.parentComponentId())
  }

  protected isNotFormValid(): string | null {
    const valueFg = this.validationValueFg
    if (valueFg.touched &&
      valueFg.hasError('rangeInputs') &&
      valueFg.controls.fromNumber.touched &&
      valueFg.controls.fromNumber.dirty &&
      valueFg.controls.toNumber.touched &&
      valueFg.controls.toNumber.dirty
    ) {
      return `From number must be greater than to number`
    }
    return null
  }

  private setupResponseValidationForm() {
    const vCon = this.question().validationConfig
    const vId = vCon.validationId
    if (vId !== 'SHORT_ANSWER_NONE') {

      const validationIdMeta = this.constant.getByValidationId(vId)

      this.validationValidationTypes.set(this.constant.getValidationTypes(validationIdMeta.input.value))

      this.validationSelectorFg.patchValue({
        inputType: validationIdMeta.input.value,
        validationType: validationIdMeta.validation.value
      })

      this.validationValueFg.patchValue({...vCon})

      this.activeValidationInputId.set(this.constant.getByValidationId(vId).activeValidationInputId)

      this.moreMenuItemId.emit('responseValidation')
    }
  }

  private getValidationConfig(): AnyShortAnswerValidationConfig {
    if (!this.showResponseValidation()) {
      return {validationId: 'SHORT_ANSWER_NONE', errorText: null};
    }

    const validationId = this.getValidationId()
    const activeValidationInput = this.constant.getByValidationId(validationId).activeValidationInputId

    const {number, fromNumber, toNumber, text, errorText} = this.validationValueFg.value
    const common = {validationId: validationId, errorText: errorText ?? null}

    switch (activeValidationInput) {
      case 'NUMBER':
        return {...common, number: number!}
      case 'FROM_TO_NUMBER':
        return {...common, fromNumber: fromNumber!, toNumber: toNumber!}
      case 'PATTERN':
        return {...common, text: text!}
      case null:
        return common
    }
  }

  private getValidationId(): ValidationId {
    const questionType: QuestionType = 'SHORT_ANSWER'
    const inputType = this.validationSelectorFg.value.inputType
    const validationType = this.validationSelectorFg.value.validationType

    return `${questionType}_${inputType}_${validationType}` as ValidationId
  }

  protected emitCanSaveAndHasError() {
    let isFormInvalid: boolean
    let canSave: boolean
    const validationId = this.getValidationId()
    const activeValidationInput = this.constant.getByValidationId(validationId).activeValidationInputId
    const controls = this.validationValueFg.controls
    switch (activeValidationInput) {
      case 'NUMBER': {
        const control = controls.number
        isFormInvalid = control.touched && control.invalid
        canSave = control.valid
        break
      }
      case 'FROM_TO_NUMBER': {
        const fromNumControl = controls.fromNumber
        const toNumControl = controls.toNumber
        const isFromNumberValid = fromNumControl.touched && fromNumControl.invalid
        const isToNumberValid = toNumControl.touched && toNumControl.invalid
        isFormInvalid = isFromNumberValid || isToNumberValid
        canSave = fromNumControl.valid && toNumControl.valid
        break
      }
      case 'PATTERN': {
        const control = controls.text
        isFormInvalid = control.touched && control.invalid
        canSave = control.valid
        break
      }
      case null: {
        isFormInvalid = false
        canSave = true
        break
      }
    }

    if (this.moreMenuItemIds().has('responseValidation')) {
      this.hasError.emit(isFormInvalid)
      this.canSaveQuestion.emit(canSave)
    }
  }
}
