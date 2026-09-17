import { ComplexQuestionUpdateAction } from "../../../../type/complex-question-update-action";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface DropdownUpdateReq extends OnlyDropdownUpdateReq, QuestionUpdateReq {

}

export interface OnlyDropdownUpdateReq extends OnlyQuestionUpdateReq {
    option: DropdownOptionUpdateReq
}

export interface DropdownOptionUpdateReq {
    id?: string,
    option?: string,
    action: ComplexQuestionUpdateAction
}