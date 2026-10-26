import { FormDefaultSettings } from './form-default-settings';
import { FormGeneralSettings } from './form-general-settings';
import { FormPresentationSettings } from './form-presentation-settings';
import { FormQuizSettings } from './form-quiz-settings';
import { FormResponseSettings } from './form-response-settings';

export interface FormInfo {
  id: string;
  name: string | null;
  title: string | null;
  description: string | null;
  lastOpenedOn: string;
  defaultSettings: FormDefaultSettings;
  generalSettings: FormGeneralSettings;
  presentationSettings: FormPresentationSettings;
  quizSettings: FormQuizSettings;
  responseSettings: FormResponseSettings;
}
