import {QuestionType} from '../../../type/question-type';

export interface QuestionResponsePutReq {
  questionId: string,
  questionType: QuestionType
}
