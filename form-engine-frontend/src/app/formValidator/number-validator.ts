import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';
import {isNumber} from 'lodash';

export class NumberValidator {

  static positive: ValidatorFn = (control: AbstractControl<number | null>): ValidationErrors | null => {
    if (control.value && control.value >= 0) return null
    return {positive: true}
  }

  static negative: ValidatorFn = (control: AbstractControl<number | null>): ValidationErrors | null => {
    if (control.value && control.value < 0) return null
    return {negative: true}
  }

  static lessThan(num: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (control.value && control.value < num) return null
      return {lessThan: {lessThan: num, actual: control.value}}
    }
  }

  static lessThanOrEqualTo(num: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (control.value && control.value <= num) return null
      return {lessThanOrEqualTo: {lessThanOrEqualTo: num, actual: control.value}}
    }
  }

  static greaterThan(num: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (control.value && control.value > num) return null
      return {greaterThan: {greaterThan: num, actual: control.value}}
    }
  }

  static greaterThanOrEqualTo(num: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (control.value && control.value >= num) return null
      return {greaterThanOrEqualTo: {greaterThanOrEqualTo: num, actual: control.value}}
    }
  }

  static equalTo(num: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (control.value === num) return null
      return {equalTo: {equalTo: num, actual: control.value}}
    }
  }

  static notEqualTo(num: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (control.value !== num) return null
      return {notEqualTo: {notEqualTo: num, actual: control.value}}
    }
  }

  static between(fromNum: number, toNum: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (control.value && control.value >= fromNum && control.value <= toNum) return null
      return {between: {fromNum: fromNum, toNum: toNum, actual: control.value}}
    }
  }

  static notBetween(fromNum: number, toNum: number): ValidatorFn {
    return (control: AbstractControl<number | null>): ValidationErrors | null => {
      if (!(control.value && control.value >= fromNum && control.value <= toNum)) return null
      return {notBetween: {fromNum: fromNum, toNum: toNum, actual: control.value}}
    }
  }

  static wholeNumber: ValidatorFn = (control: AbstractControl<number | null>): ValidationErrors | null => {
    if (control.value && Number.isInteger(control.value) && control.value >= 0) return null
    return {wholeNumber: true}
  }

  static isNumber: ValidatorFn = (control: AbstractControl<number | null>): ValidationErrors | null => {
    if (isNumber(control.value)) return null
    return {wholeNumber: true}
  }

}
