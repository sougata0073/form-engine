import {OnlyCheckboxAddUpdateReq} from '../model/edit-form/question/request/checkbox-add-update-req';
import {OnlyDateAddUpdateReq} from '../model/edit-form/question/request/date-add-update-req';
import {OnlyDateTimeAddUpdateReq} from '../model/edit-form/question/request/date-time-add-update-req';
import {OnlyDropdownAddUpdateReq} from '../model/edit-form/question/request/dropdown-add-update-req';
import {OnlyDurationAddUpdateReq} from '../model/edit-form/question/request/duration-add-update-req';
import {OnlyFileUploadAddUpdateReq} from '../model/edit-form/question/request/file-upload-add-update-req';
import {OnlyLinearScaleAddUpdateReq} from '../model/edit-form/question/request/linear-scale-add-update-req';
import {OnlyMultipleChoiceAddUpdateReq} from '../model/edit-form/question/request/multiple-choice-add-update-req';
import {
  OnlyMultipleChoiceGridAddUpdateReq
} from '../model/edit-form/question/request/multiple-choice-grid-add-update-req';
import {OnlyParagraphAddUpdateReq} from '../model/edit-form/question/request/paragraph-add-update-req';
import {OnlyRatingAddUpdateReq} from '../model/edit-form/question/request/rating-add-update-req';
import {OnlyShortAnswerAddUpdateReq} from '../model/edit-form/question/request/short-answer-add-update-req';
import {OnlyTickBoxGridAddUpdateReq} from '../model/edit-form/question/request/tick-box-grid-add-update-req';
import {OnlyTimeAddUpdateReq} from '../model/edit-form/question/request/time-add-update-req';
import {AnyCheckboxValidationConfig} from './any-checkbox-validation-config';
import {AnyParagraphValidationConfig} from './any-paragraph-validation-config';
import {AnyShortAnswerValidationConfig} from './any-short-answer-validation-config';

export type AnyOnlyQuestionAddUpdateReq =
  OnlyCheckboxAddUpdateReq<AnyCheckboxValidationConfig>
  | OnlyDateAddUpdateReq
  | OnlyDateTimeAddUpdateReq
  | OnlyDropdownAddUpdateReq
  | OnlyDurationAddUpdateReq
  | OnlyFileUploadAddUpdateReq
  | OnlyLinearScaleAddUpdateReq
  | OnlyMultipleChoiceAddUpdateReq
  | OnlyMultipleChoiceGridAddUpdateReq
  | OnlyParagraphAddUpdateReq<AnyParagraphValidationConfig>
  | OnlyRatingAddUpdateReq
  | OnlyShortAnswerAddUpdateReq<AnyShortAnswerValidationConfig>
  | OnlyTickBoxGridAddUpdateReq
  | OnlyTimeAddUpdateReq
