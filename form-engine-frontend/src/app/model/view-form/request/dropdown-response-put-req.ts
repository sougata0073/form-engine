import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyDropdownResponsePutReq {
  responseOptionId: string
}

export interface DropdownResponsePutReq extends QuestionResponsePutReq, OnlyDropdownResponsePutReq {
}
