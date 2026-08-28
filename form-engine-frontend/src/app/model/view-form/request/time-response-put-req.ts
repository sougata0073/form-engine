import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyTimeResponsePutReq {
  time: Date
}

export interface TimeResponsePutReq extends QuestionResponsePutReq, OnlyTimeResponsePutReq {

}
