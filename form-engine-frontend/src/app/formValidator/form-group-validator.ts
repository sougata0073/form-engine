import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export class FormGroupValidator {

  static rangeInputs(fromInputName: string, toInputNumber: string): ValidatorFn {
    return (group: AbstractControl): ValidationErrors | null => {
      const fromNumber = Number(group.get(fromInputName)?.value)
      const toNumber = Number(group.get(toInputNumber)?.value)

      if (fromNumber < toNumber) return null

      return {rangeInputs: {fromNumber: fromNumber, toNumber: toNumber}}
    }
  }

  static bothFieldSameValidator(field1: AbstractControl, field2: AbstractControl): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const field1Val = field1.value
      const field2Val = field2.value

      if (field1Val === field2Val) return null

      return {bothFieldSame: true}
    }
  }

}
