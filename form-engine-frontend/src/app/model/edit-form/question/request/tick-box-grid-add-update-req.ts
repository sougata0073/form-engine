import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyTickBoxGridAddUpdateReq {
  eachRowRequired: boolean,
  rows: { id: string | null, row: string }[],
  columns: { id: string | null, column: string }[]
}

export interface TickBoxGridAddUpdateReq extends QuestionAddUpdateReq, OnlyTickBoxGridAddUpdateReq {
}

