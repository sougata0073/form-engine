import {ParagraphValidationConfig} from '../model/validation-config/paragraph-validation-config';
import {ValidationConfig} from '../model/validation-config/validation-config';

export type AnyParagraphValidationConfig =
  ValidationConfig
  | ParagraphValidationConfig.MaxCharacterCount
  | ParagraphValidationConfig.MinCharacterCount
  | ParagraphValidationConfig.Matches
  | ParagraphValidationConfig.DoesNotMatch
