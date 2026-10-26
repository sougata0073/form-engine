import { FormEmailCollectionOption } from '../../type/form-email-collection-option';
import { ResponseCopySendOption } from '../../type/response-copy-send-option';
import { PatchReq } from '../common/patch-req';

export interface FormResponseSettingsUpdateReq extends PatchReq {
  emailCollectionOption?: FormEmailCollectionOption;
  responseCopySendOption?: ResponseCopySendOption;
  isResponseEditable?: boolean;
  singleResponseOnly?: boolean;
}
