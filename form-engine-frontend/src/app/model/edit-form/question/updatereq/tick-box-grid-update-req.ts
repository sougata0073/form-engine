import { ComplexQuestionUpdateAction } from "../../../../type/complex-question-update-action";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface TickBoxGridUpdateReq extends OnlyTickBoxGridUpdateReq, QuestionUpdateReq {

}

export interface OnlyTickBoxGridUpdateReq extends OnlyQuestionUpdateReq {
    eachRowRequired?: boolean
    row?: TickBoxGridRowUpdateReq,
    column?: TickBoxGridColumnUpdateReq
}

export interface TickBoxGridRowUpdateReq {
    id?: string,
    row?: string,
    action: ComplexQuestionUpdateAction
}

export interface TickBoxGridColumnUpdateReq {
    id?: string,
    column?: string,
    action: ComplexQuestionUpdateAction
}