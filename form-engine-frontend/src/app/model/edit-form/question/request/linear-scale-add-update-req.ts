import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyLinearScaleAddUpdateReq {
  fromNumber: number,
  toNumber: number
}

export interface LinearScaleAddUpdateReq extends QuestionAddUpdateReq, OnlyLinearScaleAddUpdateReq {
}

