import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface DateTimeUpdateReq extends OnlyDateTimeUpdateReq, QuestionUpdateReq {

}

export interface OnlyDateTimeUpdateReq extends OnlyQuestionUpdateReq {

}