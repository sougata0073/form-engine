import {ValidationId} from '../../type/validation-id';
import {QuestionType} from '../../type/question-type';

export interface ValidationReq {
  questionId: number,
  validationId: ValidationId,
  questionType: QuestionType
}
