import { AnyCheckboxValidationConfig } from "../../../../type/any-checkbox-validation-config";
import { ComplexQuestionUpdateAction } from "../../../../type/complex-question-update-action";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface CheckboxUpdateReq extends OnlyCheckboxUpdateReq, QuestionUpdateReq {

}

export interface OnlyCheckboxUpdateReq extends OnlyQuestionUpdateReq {
    options?: CheckboxOptionUpdateReq[],
    validationConfig?: AnyCheckboxValidationConfig
}

export interface CheckboxOptionUpdateReq {
    id?: string,
    option?: string,
    action: ComplexQuestionUpdateAction
}