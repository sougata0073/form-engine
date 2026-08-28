import {QuestionType} from '../../../../type/question-type';

export interface ResponseSummary {
  questionId: string,
  question: string,
  orderIndex: number,
  questionType: QuestionType,
  numberOfResponses: string
}
