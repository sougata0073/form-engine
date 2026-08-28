import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyLinearScaleResponsePutReq {
  scale: number
}

export interface LinearScaleResponsePutReq extends QuestionResponsePutReq, OnlyLinearScaleResponsePutReq {

}
