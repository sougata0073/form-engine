import {ValidationReq} from './validation-req';

export interface DurationValidationReq extends ValidationReq {
  hours: number,
  minutes: number,
  seconds: number
}
