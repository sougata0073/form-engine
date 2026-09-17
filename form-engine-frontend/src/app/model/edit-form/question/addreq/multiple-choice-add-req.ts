import {QuestionAddReq} from './question-add-req';

export interface OnlyMultipleChoiceAddUpdateReq {
  options: { id: string | null, option: string }[]
}

export interface MultipleChoiceAddUpdateReq extends QuestionAddReq, OnlyMultipleChoiceAddUpdateReq {
}

