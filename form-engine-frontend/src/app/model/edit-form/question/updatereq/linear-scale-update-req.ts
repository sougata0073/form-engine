import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface LinearScaleUpdateReq extends OnlyLinearScaleUpdateReq, QuestionUpdateReq {

}

export interface OnlyLinearScaleUpdateReq extends OnlyQuestionUpdateReq {
    fromNumber?: number,
    toNumber?: number
}