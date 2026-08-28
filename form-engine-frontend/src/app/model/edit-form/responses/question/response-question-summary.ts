import {QuestionType} from '../../../../type/question-type';

export interface ResponseQuestionSummary {
  questionId: string,
  question: string | null,
  questionType: QuestionType
}
