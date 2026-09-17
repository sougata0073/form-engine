import {QuestionAddReq} from './question-add-req';

export interface OnlyDropdownAddUpdateReq {
  options: { id: string | null, option: string }[]
}

export interface DropdownAddUpdateReq extends QuestionAddReq, OnlyDropdownAddUpdateReq {
}

