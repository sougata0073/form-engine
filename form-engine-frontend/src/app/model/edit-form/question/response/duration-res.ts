import {QuestionRes} from './question-res';

export interface OnlyDurationRes {
}

export interface DurationRes extends OnlyDurationRes, QuestionRes {
}
