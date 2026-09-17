import { AnyShortAnswerValidationConfig } from "../../../../type/any-short-answer-validation-config";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface ShortAnswerUpdateReq extends OnlyShortAnswerUpdateReq, QuestionUpdateReq {

}

export interface OnlyShortAnswerUpdateReq extends OnlyQuestionUpdateReq {
    validationConfig?: AnyShortAnswerValidationConfig
}