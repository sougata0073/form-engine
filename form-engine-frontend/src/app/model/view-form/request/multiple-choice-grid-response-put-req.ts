import {QuestionResponsePutReq} from './question-response-put-req';

export interface OnlyMultipleChoiceGridResponsePutReq {
  rows: OnlyMultipleChoiceGridRowResponsePutReq[]
}

export interface OnlyMultipleChoiceGridRowResponsePutReq {
  rowId: string,
  responseColumnId: string
}

export interface MultipleChoiceGridResponsePutReq extends QuestionResponsePutReq, OnlyMultipleChoiceGridResponsePutReq {

}
