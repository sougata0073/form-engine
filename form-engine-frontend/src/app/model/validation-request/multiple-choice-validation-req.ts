import {ValidationReq} from './validation-req';

export interface MultipleChoiceValidationReq extends ValidationReq {
  responseIndex: number
}
