import {QuestionAddReq} from './question-add-req';

export interface OnlyLinearScaleAddUpdateReq {
  fromNumber: number,
  toNumber: number
}

export interface LinearScaleAddUpdateReq extends QuestionAddReq, OnlyLinearScaleAddUpdateReq {
}

