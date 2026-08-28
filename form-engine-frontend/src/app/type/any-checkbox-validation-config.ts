import {CheckboxValidationConfig} from '../model/validation-config/checkbox-validation-config';
import {ValidationConfig} from '../model/validation-config/validation-config';

export type AnyCheckboxValidationConfig =
  ValidationConfig
  | CheckboxValidationConfig.SelectAtLeast
  | CheckboxValidationConfig.SelectAtMost
  | CheckboxValidationConfig.SelectExactly
