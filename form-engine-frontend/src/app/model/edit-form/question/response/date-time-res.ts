import {QuestionRes} from './question-res';

export interface OnlyDateTimeRes {
}

export interface DateTimeRes extends OnlyDateTimeRes, QuestionRes {
}
