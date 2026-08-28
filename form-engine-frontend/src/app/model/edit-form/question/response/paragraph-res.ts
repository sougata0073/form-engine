import {QuestionRes} from './question-res';
import {ValidationConfig} from '../../../validation-config/validation-config';

export interface OnlyParagraphRes<VC extends ValidationConfig> {
  validationConfig: VC
}

export interface ParagraphRes<VC extends ValidationConfig> extends OnlyParagraphRes<VC>, QuestionRes {
}

