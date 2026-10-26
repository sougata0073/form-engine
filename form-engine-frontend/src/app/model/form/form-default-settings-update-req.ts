import { FormEmailCollectionOption } from '../../type/form-email-collection-option';
import { PatchReq } from '../common/patch-req';

export interface FormDefaultSettingsUpdateReq extends PatchReq {
  defaultEmailCollectionOption?: FormEmailCollectionOption;
  makeQuestionsRequiredByDefault?: boolean;
}
