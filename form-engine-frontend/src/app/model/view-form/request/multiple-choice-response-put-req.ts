import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyMultipleChoiceResponsePutReq {
  responseOptionId: string
}

export interface MultipleChoiceResponsePutReq extends QuestionResponsePutReq, OnlyMultipleChoiceResponsePutReq {

}
