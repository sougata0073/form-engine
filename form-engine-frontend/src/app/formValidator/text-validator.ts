import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export class TextValidator {

  static contains(text: string): ValidatorFn {
    return (control: AbstractControl<string | null>): ValidationErrors | null => {
      return control.value?.includes(text) ? null : {contains: true}
    }
  }

  static doesNotContains(text: string): ValidatorFn {
    return (control: AbstractControl<string | null>): ValidationErrors | null => {
      return !control.value?.includes(text) ? null : {contains: true}
    }
  }

  static url: ValidatorFn = (control: AbstractControl<string | null>): ValidationErrors | null => {
    const urlRegex =
      '^(https?:\\/\\/)?((localhost)|(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}))(:\\d+)?(\\/[^\\s]*)?$';

    return new RegExp(urlRegex).test(control.value ?? '') ? null : {url: true}
  }

}
