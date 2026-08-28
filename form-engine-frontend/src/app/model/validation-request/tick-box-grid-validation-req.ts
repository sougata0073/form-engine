import {ValidationReq} from './validation-req';

export interface TickBoxGridValidationReq extends ValidationReq {
  rows: TickBoxGridRowValidationReq[]
}

export interface TickBoxGridRowValidationReq {
  responseIndexes: number[]
}
