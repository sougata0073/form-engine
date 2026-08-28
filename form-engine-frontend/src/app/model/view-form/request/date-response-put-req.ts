import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyDateResponsePutReq {
  date: Date
}

export interface DateResponsePutReq extends QuestionResponsePutReq, OnlyDateResponsePutReq {
}
