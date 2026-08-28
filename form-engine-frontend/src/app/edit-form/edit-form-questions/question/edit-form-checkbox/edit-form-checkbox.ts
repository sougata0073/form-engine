import {Component, inject, OnChanges, OnInit, signal, SimpleChanges} from '@angular/core';
import {CheckboxRes} from '../../../../model/edit-form/question/response/checkbox-res';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {AnyCheckboxValidationConfig} from '../../../../type/any-checkbox-validation-config';
import {EditFormCheckboxOption} from './edit-form-checkbox-option/edit-form-checkbox-option';
import {MatButton} from '@angular/material/button';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {CheckboxActiveValidationInputId, CheckboxConstant} from '../../../../constant/checkbox-constant';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {ValidationId} from '../../../../type/validation-id';
import {QuestionType} from '../../../../type/question-type';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {MatDialog} from '@angular/material/dialog';
import {SimpleDialog} from '../../../../shared/simple-dialog/simple-dialog';
import {CheckboxOption} from '../../../../type/checkbox-option';
import {OnlyCheckboxAddUpdateReq} from '../../../../model/edit-form/question/request/checkbox-add-update-req';

@Component({
  selector: 'app-edit-form-checkbox',
  imports: [
    EditFormCheckboxOption,
    MatButton,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-checkbox.html',
  styleUrl: './edit-form-checkbox.scss',
})
export class EditFormCheckbox
  extends EditFormQuestionComponent<CheckboxRes<AnyCheckboxValidationConfig>, OnlyCheckboxAddUpdateReq<AnyCheckboxValidationConfig>>
  implements OnInit, OnChanges {

  private constant = new CheckboxConstant()

  protected validationValidationTypes = signal(this.constant.getValidationTypes())
  protected activeValidationInputId = signal<CheckboxActiveValidationInputId>(this.constant.DEFAULT_VALIDATION_ID_META.activeValidationInputId)

  protected options = signal<CheckboxOption[]>([])
  protected areAllOptionsValid = signal<boolean>(false)

  protected validationSelectorFg = new FormGroup({
    validationType: new FormControl<string>(this.constant.DEFAULT_VALIDATION_ID_META.validation.value, [Validators.required])
  })

  protected validationValueFg = new FormGroup({
    errorText: new FormControl<string | null>(null),
    number: new FormControl<number | null>(null, [Validators.required])
  })

  protected formStateService = inject(EditFormStateService)
  private dialog = inject(MatDialog)

  ngOnInit() {
    this.options.set(
      this.question().options
        .map((op) => ({...op, valid: !!op.option}))
    )

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
          validationType: this.constant.DEFAULT_VALIDATION_ID_META.validation.value
        })
        this.validationValueFg.reset()
        this.validationValueFg.markAllAsTouched()
      }

      this.emitCanSaveAndHasError()
    }
  }

  override getOnlyQuestionAddUpdateReq(): OnlyCheckboxAddUpdateReq<AnyCheckboxValidationConfig> {
    return {
      options: this.options().map(val => {
        return {
          id: val.id.startsWith('NEW_') ? null : val.id,
          option: val.option
        }
      }),
      validationConfig: this.getValidationConfig()
    }
  }

  protected showResponseValidation(): boolean {
    return this.moreMenuItemIds().has('responseValidation') && this.formStateService.isFocused(this.parentComponentId())
  }

  protected onAddOptionClick() {
    if (this.options().length >= 20) {
      this.dialog.open(
        SimpleDialog, {
          data: SimpleDialog.configure('Error', 'Can not add more than 20 options', 'Close')
        }
      )
      return
    }
    this.options.update(val => {
      const option = {
        id: 'NEW_' + crypto.randomUUID(),
        option: `Option ${val.length + 1}`,
        orderIndex: val.length,
        valid: true
      }

      return [...val, option]
    })
    this.emitCanSaveAndHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected removeOption(optionId: string) {
    this.options.update(val =>
      [...val.filter(v => v.id !== optionId)]
    )
    this.emitCanSaveAndHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onOptionTextChange(option: CheckboxOption) {
    this.options.update(val =>
      val.map(v => v.id === option.id ? {...v, option: option.option} : v)
    )
    this.emitCanSaveAndHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
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
      case null: {
        isFormInvalid = false
        canSave = true
        break
      }
    }

    const allOptionsValid = this.options().every(op => op.valid)
    if (this.moreMenuItemIds().has('responseValidation')) {
      this.hasError.emit(isFormInvalid || !allOptionsValid)
      this.canSaveQuestion.emit(canSave && allOptionsValid)
    } else {
      this.hasError.emit(!allOptionsValid)
      this.canSaveQuestion.emit(allOptionsValid)
    }
  }

  protected onOptionCanSaveChange(optionId: string, canSave: boolean) {
    this.options.update(ops => {
      return ops.map(op => {
        return op.id === optionId ? {...op, valid: canSave} : {...op}
      })
    })
    this.emitCanSaveAndHasError()
  }

  private setupResponseValidationForm() {
    const vCon = this.question().validationConfig
    const vId = vCon.validationId

    if (vId !== 'CHECKBOX_NONE') {

      const validationIdMeta = this.constant.getByValidationId(vId)

      this.validationValidationTypes.set(this.constant.getValidationTypes(validationIdMeta.input.value))

      this.validationSelectorFg.patchValue({
        validationType: validationIdMeta.validation.value
      })

      this.validationValueFg.patchValue({...vCon})

      this.activeValidationInputId.set(this.constant.getByValidationId(vId).activeValidationInputId)

      this.moreMenuItemId.emit('responseValidation')
    }
  }

  private getValidationConfig(): AnyCheckboxValidationConfig {
    if (!this.showResponseValidation()) {
      return {validationId: 'CHECKBOX_NONE', errorText: null};
    }

    const validationId = this.getValidationId()
    const activeValidationInput = this.constant.getByValidationId(validationId).activeValidationInputId

    const {number, errorText} = this.validationValueFg.value
    const common = {validationId: validationId, errorText: errorText ?? null}

    switch (activeValidationInput) {
      case 'NUMBER':
        return {...common, number: number!}
      case null:
        return common
    }
  }

  private getValidationId(): ValidationId {
    const questionType: QuestionType = 'CHECKBOX'
    const validationType = this.validationSelectorFg.value.validationType

    return `${questionType}_${validationType}` as ValidationId
  }

}
