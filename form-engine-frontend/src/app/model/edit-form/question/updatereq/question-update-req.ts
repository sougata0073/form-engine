import { AnyQuestionAddReq } from "../../../../type/any-question-add-req";
import { QuestionType } from "../../../../type/question-type";

export interface QuestionUpdateReq {
    questionId: string,
    question?: string | null,
    description?: string | null,
    required?: boolean,
    questionType: QuestionType,
    updateFields: string[],
    addReqForQuestionTypeUpdate?: AnyQuestionAddReq
}