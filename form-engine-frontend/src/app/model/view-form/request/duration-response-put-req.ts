import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyDurationResponsePutReq {
  hours: number,
  minutes: number,
  seconds: number
}

export interface DurationResponsePutReq extends QuestionResponsePutReq, OnlyDurationResponsePutReq {
}
