import {QuestionAddReq} from '../addreq/question-add-req';
import {QuestionType} from '../../../../type/question-type';

export interface QuestionRes {
  id: string,
  question: string | null,
  description: string | null,
  required: boolean,
  questionType: QuestionType
  orderIndex: number
}
