import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {CheckboxRes} from '../../../model/edit-form/question/response/checkbox-res';
import {AnyCheckboxValidationConfig} from '../../../type/any-checkbox-validation-config';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule, ValidatorFn} from '@angular/forms';
import {FormArrayValidator} from '../../../formValidator/form-array-validator';
import {CheckboxValidationConfig} from '../../../model/validation-config/checkbox-validation-config';
import {MatCheckbox} from '@angular/material/checkbox';
import {MatButton} from '@angular/material/button';
import {ValueLabelPair} from '../../../type/value-label-pair';
import {MatError} from '@angular/material/input';
import {OnlyCheckboxResponsePutReq} from '../../../model/view-form/request/checkbox-response-put-req';

@Component({
  selector: 'app-view-form-checkbox',
  imports: [
    MatCheckbox,
    MatButton,
    ReactiveFormsModule,
    MatError,
  ],
  templateUrl: './view-form-checkbox.html',
  styleUrl: './view-form-checkbox.scss',
})
export class ViewFormCheckbox
  extends ViewFormQuestionComponent<CheckboxRes<AnyCheckboxValidationConfig>, OnlyCheckboxResponsePutReq> {

  protected options =
    signal<(ValueLabelPair & { control: FormControl<boolean | null> })[]>([])

  protected showClearSelection = signal<boolean | null>(null)

  protected formValidator: ValidatorFn | null = null
  protected formError = signal<string | null>(null)

  override formGroup = new FormGroup({
    options: new FormArray<FormControl<boolean | null>>([])
  })

  override ngOnInit() {
    super.ngOnInit();

    this.options.set(
      this.question().options.map(op => {
        const option = {
          value: op.id,
          label: op.option,
          control: new FormControl<boolean | null>(null)
        }

        this.formGroup.controls.options.push(option.control)
        return option
      })
    )

    if (this.question().required) {
      this.formGroup.controls.options.addValidators(FormArrayValidator.requiredAtLeast(1))
    }

    this.formValidator = this.getFormValidator()
    this.formGroup.controls.options.addValidators(this.formValidator ?? [])

    this.formGroup.controls.options.valueChanges.subscribe(values => {
      this.showClearSelection.set(values.some(val => val))

      if (this.formValidator) {
        const isInvalid = this.formGroup.controls.options.invalid
        this.formError.set(
          isInvalid ? this.question().validationConfig.errorText ?? 'There is some error' : null
        )
      }
    })
  }

  override getOnlyQuestionResponsePutReq(): OnlyCheckboxResponsePutReq | null {
    const optionIds = this.options()
      .filter(op => op.control.value)
      .map(op => op.value)

    return optionIds.length === 0 ? null : {
      responseOptionIds: optionIds
    }
  }

  override clearForm() {
    this.formGroup.controls.options.controls
      .forEach(ctr => ctr.patchValue(null))
  }

  protected onClearSelectionClick() {
    this.formGroup.controls.options.controls.forEach(control => {
      control.patchValue(null)
    })
  }

  private getFormValidator(): ValidatorFn | null {
    const validationConfig = this.question().validationConfig
    switch (validationConfig.validationId) {
      case 'CHECKBOX_SELECT_AT_LEAST': {
        const vCon = validationConfig as CheckboxValidationConfig.SelectAtLeast
        return FormArrayValidator.requiredAtLeast(vCon.number);
      }
      case 'CHECKBOX_SELECT_AT_MOST': {
        const vCon = validationConfig as CheckboxValidationConfig.SelectAtMost
        return FormArrayValidator.requiredAtMost(vCon.number);
      }
      case 'CHECKBOX_SELECT_EXACTLY': {
        const vCon = validationConfig as CheckboxValidationConfig.SelectExactly
        return FormArrayValidator.requiredExactly(vCon.number);
      }
      default:
        return null
    }
  }
}
