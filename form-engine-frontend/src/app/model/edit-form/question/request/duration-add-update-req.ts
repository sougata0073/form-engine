import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyDurationAddUpdateReq {
}

export interface DurationAddUpdateReq extends QuestionAddUpdateReq, OnlyDurationAddUpdateReq {
}
