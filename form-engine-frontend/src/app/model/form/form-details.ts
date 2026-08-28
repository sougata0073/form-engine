import {AnyQuestionRes} from '../../type/any-question-res';
import {FormInfoRes} from './form-info-res';

export interface FormDetails extends FormInfoRes {
  questions: AnyQuestionRes[]
}
