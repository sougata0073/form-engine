import {QuestionRes} from './question-res';
import {ValidationConfig} from '../../../validation-config/validation-config';

export interface OnlyCheckboxRes<VC extends ValidationConfig> {
  options: OnlyCheckboxOptionRes[],
  validationConfig: VC
}

export interface CheckboxRes<VC extends ValidationConfig> extends OnlyCheckboxRes<VC>, QuestionRes {
}

export interface OnlyCheckboxOptionRes {
  id: string,
  option: string,
  orderIndex: number
}
