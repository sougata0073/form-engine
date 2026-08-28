import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyShortAnswerResponsePutReq {
  text: string
}

export interface ShortAnswerResponsePutReq extends QuestionResponsePutReq, OnlyShortAnswerResponsePutReq {

}
