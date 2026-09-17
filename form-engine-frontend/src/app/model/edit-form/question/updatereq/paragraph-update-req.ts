import { AnyParagraphValidationConfig } from "../../../../type/any-paragraph-validation-config";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface ParagraphUpdateReq extends OnlyParagraphUpdateReq, QuestionUpdateReq {

}

export interface OnlyParagraphUpdateReq extends OnlyQuestionUpdateReq {
    validationConfig?: AnyParagraphValidationConfig
}