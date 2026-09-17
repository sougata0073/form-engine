import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface DurationUpdateReq extends OnlyDurationUpdateReq, QuestionUpdateReq {

}

export interface OnlyDurationUpdateReq extends OnlyQuestionUpdateReq {
    
}