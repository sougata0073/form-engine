import {ValidationConfig} from '../../../validation-config/validation-config';
import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyShortAnswerAddUpdateReq<VC extends ValidationConfig> {
  validationConfig: VC
}

export interface ShortAnswerAddUpdateReq<VC extends ValidationConfig> extends QuestionAddUpdateReq, OnlyShortAnswerAddUpdateReq<VC> {
}

