import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyTickBoxGridResponsePutReq {
  rows: OnlyTickBoxGridRowResponsePutReq[]
}

export interface OnlyTickBoxGridRowResponsePutReq {
  rowId: string,
  responseColumnIds: string[]
}

export interface TickBoxGridResponsePutReq extends QuestionResponsePutReq, OnlyTickBoxGridResponsePutReq {

}
