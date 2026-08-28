import {QuestionRes} from './question-res';

export interface OnlyDropdownRes {
  options: OnlyDropdownOptionRes[]
}

export interface DropdownRes extends OnlyDropdownRes, QuestionRes {
}

export interface OnlyDropdownOptionRes {
  id: string,
  option: string,
  orderIndex: number
}
