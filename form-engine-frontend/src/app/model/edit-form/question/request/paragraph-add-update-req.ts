import {QuestionAddUpdateReq} from './question-add-update-req';
import {ValidationConfig} from '../../../validation-config/validation-config';

export interface OnlyParagraphAddUpdateReq<VC extends ValidationConfig> {
  validationConfig: VC
}

export interface ParagraphAddUpdateReq<VC extends ValidationConfig> extends QuestionAddUpdateReq, OnlyParagraphAddUpdateReq<VC> {
}

