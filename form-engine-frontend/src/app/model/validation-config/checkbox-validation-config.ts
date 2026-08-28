import {ValidationConfig} from './validation-config';

export namespace CheckboxValidationConfig {
  export interface SelectAtLeast extends ValidationConfig {
    number: number
  }

  export interface SelectAtMost extends ValidationConfig {
    number: number
  }

  export interface SelectExactly extends ValidationConfig {
    number: number
  }
}
