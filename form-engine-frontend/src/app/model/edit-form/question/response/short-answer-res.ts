import {QuestionRes} from './question-res';
import {ValidationConfig} from '../../../validation-config/validation-config';

export interface OnlyShortAnswerRes<VC extends ValidationConfig> {
  validationConfig: VC
}

export interface ShortAnswerRes<VC extends ValidationConfig> extends OnlyShortAnswerRes<VC>, QuestionRes {
}

