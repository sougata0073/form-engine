import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyRatingResponsePutReq {
  rating: number
}

export interface RatingResponsePutReq extends QuestionResponsePutReq, OnlyRatingResponsePutReq {

}
