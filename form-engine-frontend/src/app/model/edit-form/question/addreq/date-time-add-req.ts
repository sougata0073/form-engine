import {QuestionAddReq} from './question-add-req';

export interface OnlyDateTimeAddUpdateReq {
}

export interface DateTimeAddUpdateReq extends QuestionAddReq, OnlyDateTimeAddUpdateReq {

}
