import {ValidationReq} from './validation-req';

export interface CheckboxValidationReq extends ValidationReq {
  responseIndexes: number[]
}
