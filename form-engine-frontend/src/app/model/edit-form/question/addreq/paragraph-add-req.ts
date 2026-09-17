import {QuestionAddReq} from './question-add-req';
import {ValidationConfig} from '../../../validation-config/validation-config';

export interface OnlyParagraphAddUpdateReq<VC extends ValidationConfig> {
  validationConfig: VC
}

export interface ParagraphAddUpdateReq<VC extends ValidationConfig> extends QuestionAddReq, OnlyParagraphAddUpdateReq<VC> {
}

