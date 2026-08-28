import {ValidationReq} from './validation-req';

export interface ResponseValidationReq<T extends ValidationReq> {
  responses: T[]
}
