import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface TimeUpdateReq extends OnlyTimeUpdateReq, QuestionUpdateReq {

}

export interface OnlyTimeUpdateReq extends OnlyQuestionUpdateReq {
    
}