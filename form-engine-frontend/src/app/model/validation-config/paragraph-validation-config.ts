import {ValidationConfig} from './validation-config';

export namespace ParagraphValidationConfig {
  export interface MaxCharacterCount extends ValidationConfig {
    number: number;
  }

  export interface MinCharacterCount extends ValidationConfig {
    number: number;
  }

  export interface Matches extends ValidationConfig {
    text: string;
  }

  export interface DoesNotMatch extends ValidationConfig {
    text: string;
  }
}
