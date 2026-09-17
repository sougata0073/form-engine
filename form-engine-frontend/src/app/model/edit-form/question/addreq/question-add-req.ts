import {QuestionType} from '../../../../type/question-type';

export interface QuestionAddReq {
  question: string | null,
  description: string | null,
  required: boolean,
  questionType: QuestionType
}
