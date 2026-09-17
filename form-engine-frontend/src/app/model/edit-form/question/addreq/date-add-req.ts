import {QuestionAddReq} from './question-add-req';

export interface OnlyDateAddUpdateReq {
}

export interface DateAddUpdateReq extends QuestionAddReq, OnlyDateAddUpdateReq {

}
