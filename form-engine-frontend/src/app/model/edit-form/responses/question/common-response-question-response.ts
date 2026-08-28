import {QuestionType} from '../../../../type/question-type';

export interface CommonResponseQuestionResponse {
  questionId: string,
  questionType: QuestionType,
  responseCount: string,
  formResponsesIdentifier: string
}
