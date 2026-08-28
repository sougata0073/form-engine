import {Component, inject, OnChanges, OnInit, signal, SimpleChanges} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {ParagraphRes} from '../../../../model/edit-form/question/response/paragraph-res';
import {AnyParagraphValidationConfig} from '../../../../type/any-paragraph-validation-config';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {ParagraphActiveValidationInputId, ParagraphConstant} from '../../../../constant/paragraph-constant';
import {RegexValidator} from '../../../../formValidator/regex-validator';
import {ValidationId} from '../../../../type/validation-id';
import {QuestionType} from '../../../../type/question-type';
import {OnlyParagraphAddUpdateReq} from '../../../../model/edit-form/question/request/paragraph-add-update-req';

@Component({
  selector: 'app-edit-form-paragraph',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule,
  ],
  templateUrl: './edit-form-paragraph.html',
  styleUrl: './edit-form-paragraph.scss',
})
export class EditFormParagraph
  extends EditFormQuestionComponent<ParagraphRes<AnyParagraphValidationConfig>, OnlyParagraphAddUpdateReq<AnyParagraphValidationConfig>>
  implements OnInit, OnChanges {

  private constant = new ParagraphConstant()

  protected validationInputTypes = signal(this.constant.getInputs())
  protected validationValidationTypes = signal(this.constant.getValidationTypes(this.constant.DEFAULT_VALIDATION_ID_META.input.value))
  protected activeValidationInputId = signal<ParagraphActiveValidationInputId>(this.constant.DEFAULT_VALIDATION_ID_META.activeValidationInputId)

  private formStateService = inject(EditFormStateService)

  protected validationSelectorFg = new FormGroup({
    inputType: new FormControl<string>(this.constant.DEFAULT_VALIDATION_ID_META.input.value, [Validators.required]),
    validationType: new FormControl<string>(this.constant.DEFAULT_VALIDATION_ID_META.validation.value, [Validators.required])
  })

  protected validationValueFg = new FormGroup({
    errorText: new FormControl<string | null>(null),
    number: new FormControl<number | null>(null, [Validators.required]),
    text: new FormControl<string | null>(null, [Validators.required, RegexValidator.pattern])
  })

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

  override getOnlyQuestionAddUpdateReq(): OnlyParagraphAddUpdateReq<AnyParagraphValidationConfig> {
    return {
      validationConfig: this.getValidationConfig()
    }
  }

  protected showResponseValidation(): boolean {
    return this.moreMenuItemIds().has('responseValidation') && this.formStateService.isFocused(this.parentComponentId())
  }

  private setupResponseValidationForm() {
    const vCon = this.question().validationConfig
    const vId = vCon.validationId

    if (vId !== 'PARAGRAPH_NONE') {

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

  private getValidationConfig(): AnyParagraphValidationConfig {
    if (!this.showResponseValidation()) {
      return {validationId: 'PARAGRAPH_NONE', errorText: null};
    }

    const validationId = this.getValidationId()
    const activeValidationInput = this.constant.getByValidationId(validationId).activeValidationInputId

    const {number, text, errorText} = this.validationValueFg.value
    const common = {validationId: validationId, errorText: errorText ?? null}

    switch (activeValidationInput) {
      case 'NUMBER':
        return {...common, number: number!}
      case 'PATTERN':
        return {...common, text: text!}
      case null:
        return common
    }
  }

  private getValidationId(): ValidationId {
    const questionType: QuestionType = 'PARAGRAPH'
    const inputType = this.validationSelectorFg.value.inputType
    const validationType = this.validationSelectorFg.value.validationType

    return `${questionType}_${inputType}_${validationType}` as ValidationId
  }

  protected emitCanSaveAndHasError() {
    let isFormInvalid: boolean
    let canSave: boolean
    const validationId = this.getValidationId()
    const activeValidationInput = this.constant.getByValidationId(validationId).activeValidationInputId
    switch (activeValidationInput) {
      case 'NUMBER': {
        const control = this.validationValueFg.controls.number
        isFormInvalid = control.touched && control.invalid
        canSave = control.valid
        break
      }
      case 'PATTERN': {
        const control = this.validationValueFg.controls.text
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
