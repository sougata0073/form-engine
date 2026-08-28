import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyDropdownAddUpdateReq {
  options: { id: string | null, option: string }[]
}

export interface DropdownAddUpdateReq extends QuestionAddUpdateReq, OnlyDropdownAddUpdateReq {
}

