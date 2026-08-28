import {ValidationReq} from './validation-req';

export interface ShortAnswerValidationReq extends ValidationReq {
  text: string | null
}
