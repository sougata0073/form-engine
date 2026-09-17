import {QuestionAddReq} from './question-add-req';

export interface OnlyMultipleChoiceGridAddUpdateReq {
  eachRowRequired: boolean,
  rows: { id: string | null, row: string }[],
  columns: { id: string | null, column: string }[]
}

export interface MultipleChoiceGridAddUpdateReq extends QuestionAddReq, OnlyMultipleChoiceGridAddUpdateReq {
}

