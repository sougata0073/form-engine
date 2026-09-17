import {OnlyCheckboxAddUpdateReq} from '../model/edit-form/question/addreq/checkbox-add-req';
import {OnlyDateAddUpdateReq} from '../model/edit-form/question/addreq/date-add-req';
import {OnlyDateTimeAddUpdateReq} from '../model/edit-form/question/addreq/date-time-add-req';
import {OnlyDropdownAddUpdateReq} from '../model/edit-form/question/addreq/dropdown-add-req';
import {OnlyDurationAddUpdateReq} from '../model/edit-form/question/addreq/duration-add-req';
import {OnlyFileUploadAddUpdateReq} from '../model/edit-form/question/addreq/file-upload-add-req';
import {OnlyLinearScaleAddUpdateReq} from '../model/edit-form/question/addreq/linear-scale-add-req';
import {OnlyMultipleChoiceAddUpdateReq} from '../model/edit-form/question/addreq/multiple-choice-add-req';
import {
  OnlyMultipleChoiceGridAddUpdateReq
} from '../model/edit-form/question/addreq/multiple-choice-grid-add-req';
import {OnlyParagraphAddUpdateReq} from '../model/edit-form/question/addreq/paragraph-add-req';
import {OnlyRatingAddUpdateReq} from '../model/edit-form/question/addreq/rating-add-req';
import {OnlyShortAnswerAddUpdateReq} from '../model/edit-form/question/addreq/short-answer-add-req';
import {OnlyTickBoxGridAddUpdateReq} from '../model/edit-form/question/addreq/tick-box-grid-add-req';
import {OnlyTimeAddUpdateReq} from '../model/edit-form/question/addreq/time-add-req';
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
