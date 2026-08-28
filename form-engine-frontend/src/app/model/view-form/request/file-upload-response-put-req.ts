import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyFileUploadResponsePutReq {
  fileName: string | null,
  fileUrl: string,
  fileMimeType: string,
  fileSize: number
}

export interface FileUploadResponsePutReq extends QuestionResponsePutReq, OnlyFileUploadResponsePutReq {
}
