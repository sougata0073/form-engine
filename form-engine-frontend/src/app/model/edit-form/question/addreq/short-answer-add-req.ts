import {ValidationConfig} from '../../../validation-config/validation-config';
import {QuestionAddReq} from './question-add-req';

export interface OnlyShortAnswerAddUpdateReq<VC extends ValidationConfig> {
  validationConfig: VC
}

export interface ShortAnswerAddUpdateReq<VC extends ValidationConfig> extends QuestionAddReq, OnlyShortAnswerAddUpdateReq<VC> {
}

