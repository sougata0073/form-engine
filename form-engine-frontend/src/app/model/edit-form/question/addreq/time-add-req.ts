import {QuestionAddReq} from './question-add-req';

export interface OnlyTimeAddUpdateReq {
}

export interface TimeAddUpdateReq extends QuestionAddReq, OnlyTimeAddUpdateReq {

}
