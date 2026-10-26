import { FormEmailCollectionOption } from '../../type/form-email-collection-option';
import { ResponseCopySendOption } from '../../type/response-copy-send-option';

export interface FormResponseSettings {
  emailCollectionOption: FormEmailCollectionOption;
  responseCopySendOption: ResponseCopySendOption;
  isResponseEditable: boolean;
  singleResponseOnly: boolean;
}
