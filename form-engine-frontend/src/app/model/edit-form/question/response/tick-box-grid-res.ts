import {QuestionRes} from './question-res';

export interface OnlyTickBoxGridRes {
  rows: OnlyTickBoxGridRowRes[],
  columns: OnlyTickBoxGridColumnRes[]
}

export interface TickBoxGridRes extends OnlyTickBoxGridRes, QuestionRes {
}

export interface OnlyTickBoxGridRowRes {
  id: string,
  row: string,
  orderIndex: number
}

export interface OnlyTickBoxGridColumnRes {
  id: string,
  column: string,
  orderIndex: number
}
