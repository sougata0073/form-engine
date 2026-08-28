import {QuestionType} from '../../../../type/question-type';

export interface QuestionAddUpdateReq {
  question: string | null,
  description: string | null,
  required: boolean,
  questionType: QuestionType
}
