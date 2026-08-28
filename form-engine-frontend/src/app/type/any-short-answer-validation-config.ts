import {ShortAnswerValidationConfig} from '../model/validation-config/short-answer-validation-config';
import {ValidationConfig} from '../model/validation-config/validation-config';

export type AnyShortAnswerValidationConfig =
  ValidationConfig
  | ShortAnswerValidationConfig.Between
  | ShortAnswerValidationConfig.EqualTo
  | ShortAnswerValidationConfig.GreaterThan
  | ShortAnswerValidationConfig.GreaterThanOrEqualTo
  | ShortAnswerValidationConfig.LessThan
  | ShortAnswerValidationConfig.LessThanOrEqualTo
  | ShortAnswerValidationConfig.NotBetween
  | ShortAnswerValidationConfig.NotEqualTo
  | ShortAnswerValidationConfig.IsNumber
  | ShortAnswerValidationConfig.WholeNumber
  | ShortAnswerValidationConfig.Matches
  | ShortAnswerValidationConfig.DoesNotMatch
  | ShortAnswerValidationConfig.Contains
  | ShortAnswerValidationConfig.DoesNotContains
  | ShortAnswerValidationConfig.Url
  | ShortAnswerValidationConfig.Email
  | ShortAnswerValidationConfig.MaxCharacterCount
  | ShortAnswerValidationConfig.MinCharacterCount
