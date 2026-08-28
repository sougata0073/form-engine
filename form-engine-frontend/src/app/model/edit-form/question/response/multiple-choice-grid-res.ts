import {QuestionRes} from './question-res';

export interface OnlyMultipleChoiceGridRes {
  rows: OnlyMultipleChoiceGridRowRes[],
  columns: OnlyMultipleChoiceGridColumnRes[]
}

export interface MultipleChoiceGridRes extends OnlyMultipleChoiceGridRes, QuestionRes {
}

export interface OnlyMultipleChoiceGridRowRes {
  id: string,
  row: string,
  orderIndex: number
}

export interface OnlyMultipleChoiceGridColumnRes {
  id: string,
  column: string,
  orderIndex: number
}
