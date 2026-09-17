import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface FileUploadUpdateReq extends OnlyFileUploadUpdateReq, QuestionUpdateReq {

}

export interface OnlyFileUploadUpdateReq extends OnlyQuestionUpdateReq {
    allowedFileCategories?: Set<string>,
    maxFileSize?: number
}