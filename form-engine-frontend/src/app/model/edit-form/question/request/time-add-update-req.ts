import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyTimeAddUpdateReq {
}

export interface TimeAddUpdateReq extends QuestionAddUpdateReq, OnlyTimeAddUpdateReq {

}
