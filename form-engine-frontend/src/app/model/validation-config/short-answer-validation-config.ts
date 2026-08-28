import {ValidationConfig} from './validation-config';

export namespace ShortAnswerValidationConfig {
  export interface Between extends ValidationConfig {
    fromNumber: number;
    toNumber: number;
  }

  export interface EqualTo extends ValidationConfig {
    number: number;
  }

  export interface GreaterThan extends ValidationConfig {
    number: number;
  }

  export interface GreaterThanOrEqualTo extends ValidationConfig {
    number: number;
  }

  export interface LessThan extends ValidationConfig {
    number: number;
  }

  export interface LessThanOrEqualTo extends ValidationConfig {
    number: number;
  }

  export interface NotBetween extends ValidationConfig {
    fromNumber: number;
    toNumber: number;
  }

  export interface NotEqualTo extends ValidationConfig {
    number: number;
  }

  export interface IsNumber extends ValidationConfig {
  }

  export interface WholeNumber extends ValidationConfig {
  }

  export interface Matches extends ValidationConfig {
    text: string;
  }

  export interface DoesNotMatch extends ValidationConfig {
    text: string;
  }

  export interface Contains extends ValidationConfig {
    text: string;
  }

  export interface DoesNotContains extends ValidationConfig {
    text: string;
  }

  export interface Url extends ValidationConfig {
  }

  export interface Email extends ValidationConfig {
  }

  export interface MaxCharacterCount extends ValidationConfig {
    number: number;
  }

  export interface MinCharacterCount extends ValidationConfig {
    number: number;
  }
}
