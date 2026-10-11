import { Component, signal } from '@angular/core';
import { ViewFormQuestionComponent } from '../../../type/view-form-question-component';
import { CheckboxRes } from '../../../model/edit-form/question/response/checkbox-res';
import { AnyCheckboxValidationConfig } from '../../../type/any-checkbox-validation-config';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidatorFn,
} from '@angular/forms';
import { FormArrayValidator } from '../../../formValidator/form-array-validator';
import { CheckboxValidationConfig } from '../../../model/validation-config/checkbox-validation-config';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatButton } from '@angular/material/button';
import { ValueLabelPair } from '../../../type/value-label-pair';
import { MatError } from '@angular/material/input';
import { OnlyCheckboxResponsePutReq } from '../../../model/view-form/request/checkbox-response-put-req';

@Component({
  selector: 'app-view-form-checkbox',
  imports: [MatCheckbox, MatButton, ReactiveFormsModule, MatError],
  templateUrl: './view-form-checkbox.html',
  styleUrl: './view-form-checkbox.scss',
})
export class ViewFormCheckbox extends ViewFormQuestionComponent<
  CheckboxRes<AnyCheckboxValidationConfig>,
  OnlyCheckboxResponsePutReq
> {
  protected options = signal<(ValueLabelPair & { control: FormControl<boolean | null> })[]>([]);

  protected showClearSelection = signal<boolean | null>(null);

  protected formValidator: ValidatorFn | null = null;
  protected formError = signal<string | null>(null);

  override formGroup = new FormGroup({
    options: new FormArray<FormControl<boolean | null>>([]),
  });

  override ngOnInit() {
    super.ngOnInit();

    this.initialize();

    this.formGroup.controls.options.valueChanges.subscribe((values) => {
      this.showClearSelection.set(values.some((val) => val));

      if (this.formGroup.controls.options.invalid) {
        if (this.question().required && this.formGroup.controls.options.hasError('required')) {
          this.formError.set('This is a required question');
        } else if (this.formValidator) {
          this.formError.set(this.question().validationConfig.errorText ?? 'There are some error');
        }
        this.showError.set(true);
        this.hasError.emit(true);
      } else {
        this.showError.set(false);
        this.hasError.emit(false);
        this.formError.set(null);
      }
    });
  }

  override onQuestionInputChange(): void {
    const prevSelectedOptions = new Set(
      this.options()
        .filter((option) => option.control.value)
        .map((option) => option.value),
    );

    this.initialize();

    this.options().forEach((option) => {
      if (prevSelectedOptions.has(option.value)) {
        option.control.patchValue(true);
      }
    });
  }

  override getOnlyQuestionResponsePutReq(): OnlyCheckboxResponsePutReq | null {
    const optionIds = this.options()
      .filter((op) => op.control.value)
      .map((op) => op.value);

    return optionIds.length === 0
      ? null
      : {
          responseOptionIds: optionIds,
        };
  }

  override clearForm() {
    this.formGroup.controls.options.controls.forEach((ctr) => ctr.patchValue(null));
  }

  protected onClearSelectionClick() {
    this.formGroup.controls.options.controls.forEach((control) => {
      control.patchValue(null);
    });
  }

  private getFormValidator(): ValidatorFn | null {
    const validationConfig = this.question().validationConfig;
    switch (validationConfig.validationId) {
      case 'CHECKBOX_SELECT_AT_LEAST': {
        const vCon = validationConfig as CheckboxValidationConfig.SelectAtLeast;
        return FormArrayValidator.requiredAtLeast(vCon.number);
      }
      case 'CHECKBOX_SELECT_AT_MOST': {
        const vCon = validationConfig as CheckboxValidationConfig.SelectAtMost;
        return FormArrayValidator.requiredAtMost(vCon.number);
      }
      case 'CHECKBOX_SELECT_EXACTLY': {
        const vCon = validationConfig as CheckboxValidationConfig.SelectExactly;
        return FormArrayValidator.requiredExactly(vCon.number);
      }
      default:
        return null;
    }
  }

  private initialize() {
    const optionControlsArray = this.formGroup.controls.options;

    optionControlsArray.clear({ emitEvent: false });

    this.options.set(
      this.question().options.map((op) => ({
        value: op.id,
        label: op.option,
        control: new FormControl<boolean | null>(null),
      })),
    );

    this.options().forEach((option) => {
      optionControlsArray.push(option.control, { emitEvent: false });
    });

    this.formValidator = this.getFormValidator();
    optionControlsArray.setValidators(this.formValidator ?? []);

    if (this.question().required) {
      optionControlsArray.addValidators(FormArrayValidator.required);
    } else {
      if (optionControlsArray.hasValidator(FormArrayValidator.required)) {
        optionControlsArray.removeValidators(FormArrayValidator.required);
      }
    }
    optionControlsArray.updateValueAndValidity();
  }
}
