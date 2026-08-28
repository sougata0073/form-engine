import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyDateTimeAddUpdateReq {
}

export interface DateTimeAddUpdateReq extends QuestionAddUpdateReq, OnlyDateTimeAddUpdateReq {

}
