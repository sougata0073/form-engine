import {AbstractControl, FormArray, ValidationErrors, ValidatorFn} from '@angular/forms';

export class FormArrayValidator {

  static requiredAtLeast(num: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const formArray = control as FormArray
      let valueCount = 0
      formArray.controls.forEach(control => {
        if (control.value) {
          valueCount++
        }
      })

      return valueCount >= num ? null : {requiredAtLeast: true}
    }
  }

  static requiredAtMost(num: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const formArray = control as FormArray
      let valueCount = 0
      formArray.controls.forEach(control => {
        if (control.value) {
          valueCount++
        }
      })

      return valueCount <= num ? null : {requiredAtMost: true}
    }
  }

  static requiredExactly(num: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const formArray = control as FormArray
      let valueCount = 0
      formArray.controls.forEach(control => {
        if (control.value) {
          valueCount++
        }
      })

      return valueCount === num ? null : {requiredExactly: true}
    }
  }

}
