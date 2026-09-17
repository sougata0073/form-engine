import {QuestionAddReq} from './question-add-req';

export interface OnlyDurationAddUpdateReq {
}

export interface DurationAddUpdateReq extends QuestionAddReq, OnlyDurationAddUpdateReq {
}
