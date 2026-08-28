import {QuestionRes} from './question-res';

export interface OnlyMultipleChoiceRes {
  options: OnlyMultipleChoiceOptionRes[]
}

export interface MultipleChoiceRes extends OnlyMultipleChoiceRes, QuestionRes {
}

export interface OnlyMultipleChoiceOptionRes {
  id: string,
  option: string,
  orderIndex: number
}
