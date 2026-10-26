import { AnyQuestionRes } from '../../type/any-question-res';
import { FormInfo } from './form-info';

export interface FormDetails {
  formInfo: FormInfo;
  questions: AnyQuestionRes[];
}
