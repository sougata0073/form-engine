import {QuestionType} from '../../../../type/question-type';
import {CommonResponseQuestionResponse} from './common-response-question-response';

export interface ResponseQuestion<TResponses extends CommonResponseQuestionResponse> {
  questionId: string,
  questionType: QuestionType,
  responses: TResponses[]
}
