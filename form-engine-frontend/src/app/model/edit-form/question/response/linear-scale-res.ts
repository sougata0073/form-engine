import {QuestionRes} from './question-res';

export interface OnlyLinearScaleRes {
  fromNumber: number,
  toNumber: number
}

export interface LinearScaleRes extends OnlyLinearScaleRes, QuestionRes {
}

