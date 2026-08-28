import {QuestionRes} from './question-res';

export interface OnlyTimeRes {
}

export interface TimeRes extends OnlyTimeRes, QuestionRes {
}
