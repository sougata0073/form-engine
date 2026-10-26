import { PatchReq } from '../common/patch-req';
import { FormDefaultSettingsUpdateReq } from './form-default-settings-update-req';
import { FormGeneralSettingsUpdateReq } from './form-general-settings-update-req';
import { FormPresentationSettingsUpdateReq } from './form-presentation-settings-update-req';
import { FormQuizSettingsUpdateReq } from './form-quiz-settings-update-req';
import { FormResponseSettingsUpdateReq } from './form-response-settings-update-req';

export interface FormUpdateReq extends PatchReq {
  name?: string | null;
  title?: string | null;
  description?: string | null;
  defaultSettingsUpdateReq?: FormDefaultSettingsUpdateReq;
  generalSettingsUpdateReq?: FormGeneralSettingsUpdateReq;
  presentationSettingsUpdateReq?: FormPresentationSettingsUpdateReq;
  quizSettingsUpdateReq?: FormQuizSettingsUpdateReq;
  responseSettingsUpdateReq?: FormResponseSettingsUpdateReq;
}
