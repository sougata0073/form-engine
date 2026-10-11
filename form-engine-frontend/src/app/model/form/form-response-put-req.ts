import {AnyQuestionResponsePutReq} from '../../type/any-question-response-put-req';
import { FormDetails } from './form-details';

export interface FormResponsePutReq {
  formDetailsResponseSubmittedOn: FormDetails,
  responses: AnyQuestionResponsePutReq[]
}
