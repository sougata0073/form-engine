import { ComplexQuestionUpdateAction } from "../../../../type/complex-question-update-action";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface MultipleChoiceUpdateReq extends OnlyMultipleChoiceUpdateReq, QuestionUpdateReq {

}

export interface OnlyMultipleChoiceUpdateReq extends OnlyQuestionUpdateReq {
    option?: MultipleChoiceOptionUpdateReq
}

export interface MultipleChoiceOptionUpdateReq {
    id?: string,
    option?: string,
    action: ComplexQuestionUpdateAction
}