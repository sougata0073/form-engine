import {ValidationReq} from './validation-req';

export interface ParagraphValidationReq extends ValidationReq {
  text: string | null
}
