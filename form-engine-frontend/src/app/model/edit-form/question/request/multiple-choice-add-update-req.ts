import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyMultipleChoiceAddUpdateReq {
  options: { id: string | null, option: string }[]
}

export interface MultipleChoiceAddUpdateReq extends QuestionAddUpdateReq, OnlyMultipleChoiceAddUpdateReq {
}

