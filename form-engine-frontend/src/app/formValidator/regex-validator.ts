import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export class RegexValidator {

  static pattern: ValidatorFn = (control: AbstractControl<string | null>): ValidationErrors | null => {
    try {
      new RegExp(control.value ?? '')
      return null
    } catch {
      return {pattern: true}
    }
  }

  static matches(pattern: string): ValidatorFn {
    return (control: AbstractControl<string | null>): ValidationErrors | null => {
      return new RegExp(pattern).test(control.value ?? '') ? null : {matches: true}
    }
  }

  static doesNotMatches(pattern: string): ValidatorFn {
    return (control: AbstractControl<string | null>): ValidationErrors | null => {
      return !(new RegExp(pattern).test(control.value ?? '')) ? null : {doesNotMatches: true}
    }
  }

}
