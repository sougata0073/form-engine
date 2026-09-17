import {QuestionAddReq} from './question-add-req';

export interface OnlyFileUploadAddUpdateReq {
  allowedFileCategories: string[],
  maxFileSize: number
}

export interface FileUploadAddUpdateReq extends QuestionAddReq, OnlyFileUploadAddUpdateReq {
}
