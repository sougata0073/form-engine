import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyMultipleChoiceGridAddUpdateReq {
  eachRowRequired: boolean,
  rows: { id: string | null, row: string }[],
  columns: { id: string | null, column: string }[]
}

export interface MultipleChoiceGridAddUpdateReq extends QuestionAddUpdateReq, OnlyMultipleChoiceGridAddUpdateReq {
}

