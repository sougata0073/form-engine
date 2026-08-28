import {OnlyCheckboxRes} from '../model/edit-form/question/response/checkbox-res';
import {AnyCheckboxValidationConfig} from './any-checkbox-validation-config';
import {OnlyShortAnswerRes} from '../model/edit-form/question/response/short-answer-res';
import {AnyShortAnswerValidationConfig} from './any-short-answer-validation-config';
import {OnlyParagraphRes} from '../model/edit-form/question/response/paragraph-res';
import {AnyParagraphValidationConfig} from './any-paragraph-validation-config';
import {OnlyMultipleChoiceRes} from '../model/edit-form/question/response/multiple-choice-res';
import {OnlyDropdownRes} from '../model/edit-form/question/response/dropdown-res';
import {OnlyRatingRes} from '../model/edit-form/question/response/rating-res';
import {OnlyLinearScaleRes} from '../model/edit-form/question/response/linear-scale-res';
import {OnlyDateRes} from '../model/edit-form/question/response/date-res';
import {OnlyTimeRes} from '../model/edit-form/question/response/time-res';
import {OnlyDateTimeRes} from '../model/edit-form/question/response/date-time-res';
import {OnlyDurationRes} from '../model/edit-form/question/response/duration-res';
import {OnlyFileUploadRes} from '../model/edit-form/question/response/file-upload-res';
import {OnlyTickBoxGridRes} from '../model/edit-form/question/response/tick-box-grid-res';
import {OnlyMultipleChoiceGridRes} from '../model/edit-form/question/response/multiple-choice-grid-res';

export type AnyOnlyQuestionRes =
  OnlyCheckboxRes<AnyCheckboxValidationConfig>
  | OnlyShortAnswerRes<AnyShortAnswerValidationConfig>
  | OnlyParagraphRes<AnyParagraphValidationConfig>
  | OnlyMultipleChoiceRes
  | OnlyDropdownRes
  | OnlyRatingRes
  | OnlyLinearScaleRes
  | OnlyDateRes
  | OnlyTimeRes
  | OnlyDateTimeRes
  | OnlyDurationRes
  | OnlyFileUploadRes
  | OnlyTickBoxGridRes
  | OnlyMultipleChoiceGridRes
