import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyDateAddUpdateReq {
}

export interface DateAddUpdateReq extends QuestionAddUpdateReq, OnlyDateAddUpdateReq {

}
