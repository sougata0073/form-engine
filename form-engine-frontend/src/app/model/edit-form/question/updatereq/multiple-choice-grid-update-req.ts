import { ComplexQuestionUpdateAction } from "../../../../type/complex-question-update-action";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface MultipleChoiceGridUpdateReq extends OnlyMultipleChoiceGridUpdateReq, QuestionUpdateReq {

}

export interface OnlyMultipleChoiceGridUpdateReq extends OnlyQuestionUpdateReq {
    eachRowRequired?: boolean
    rows?: MultipleChoiceGridRowUpdateReq[],
    columns?: MultipleChoiceGridColumnUpdateReq[]
}

export interface MultipleChoiceGridRowUpdateReq {
    id?: string,
    row?: string,
    action: ComplexQuestionUpdateAction
}

export interface MultipleChoiceGridColumnUpdateReq {
    id?: string,
    column?: string,
    action: ComplexQuestionUpdateAction
}