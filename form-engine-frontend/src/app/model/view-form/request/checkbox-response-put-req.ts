import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyCheckboxResponsePutReq {
  responseOptionIds: string[]
}

export interface CheckboxResponsePutReq extends QuestionResponsePutReq, OnlyCheckboxResponsePutReq {
}
