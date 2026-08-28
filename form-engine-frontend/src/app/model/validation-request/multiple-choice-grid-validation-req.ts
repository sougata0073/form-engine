import {ValidationReq} from './validation-req';

export interface MultipleChoiceGridValidationReq extends ValidationReq {
  rows: MultipleChoiceGridRowValidationRequest[]
}

export interface MultipleChoiceGridRowValidationRequest {
  responseIndex: number
}
