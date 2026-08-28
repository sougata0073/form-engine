import {QuestionAddUpdateReq} from './question-add-update-req';

export interface OnlyFileUploadAddUpdateReq {
  allowedFileCategories: string[],
  maxFileSize: number
}

export interface FileUploadAddUpdateReq extends QuestionAddUpdateReq, OnlyFileUploadAddUpdateReq {
}
