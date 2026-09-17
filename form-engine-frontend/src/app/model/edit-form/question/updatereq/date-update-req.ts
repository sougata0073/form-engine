import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface DateUpdateReq extends OnlyDateUpdateReq, QuestionUpdateReq {

}

export interface OnlyDateUpdateReq extends OnlyQuestionUpdateReq {

}