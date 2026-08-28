import {CheckboxAddUpdateReq} from '../model/edit-form/question/request/checkbox-add-update-req';
import {AnyCheckboxValidationConfig} from './any-checkbox-validation-config';
import {DateAddUpdateReq} from '../model/edit-form/question/request/date-add-update-req';
import {DateTimeAddUpdateReq} from '../model/edit-form/question/request/date-time-add-update-req';
import {DropdownAddUpdateReq} from '../model/edit-form/question/request/dropdown-add-update-req';
import {DurationAddUpdateReq} from '../model/edit-form/question/request/duration-add-update-req';
import {FileUploadAddUpdateReq} from '../model/edit-form/question/request/file-upload-add-update-req';
import {LinearScaleAddUpdateReq} from '../model/edit-form/question/request/linear-scale-add-update-req';
import {MultipleChoiceAddUpdateReq} from '../model/edit-form/question/request/multiple-choice-add-update-req';
import {MultipleChoiceGridAddUpdateReq} from '../model/edit-form/question/request/multiple-choice-grid-add-update-req';
import {ParagraphAddUpdateReq} from '../model/edit-form/question/request/paragraph-add-update-req';
import {AnyParagraphValidationConfig} from './any-paragraph-validation-config';
import {RatingAddUpdateReq} from '../model/edit-form/question/request/rating-add-update-req';
import {ShortAnswerAddUpdateReq} from '../model/edit-form/question/request/short-answer-add-update-req';
import {AnyShortAnswerValidationConfig} from './any-short-answer-validation-config';
import {TickBoxGridAddUpdateReq} from '../model/edit-form/question/request/tick-box-grid-add-update-req';
import {TimeAddUpdateReq} from '../model/edit-form/question/request/time-add-update-req';

export type AnyQuestionAddUpdateReq =
  CheckboxAddUpdateReq<AnyCheckboxValidationConfig>
  | DateAddUpdateReq
  | DateTimeAddUpdateReq
  | DropdownAddUpdateReq
  | DurationAddUpdateReq
  | FileUploadAddUpdateReq
  | LinearScaleAddUpdateReq
  | MultipleChoiceAddUpdateReq
  | MultipleChoiceGridAddUpdateReq
  | ParagraphAddUpdateReq<AnyParagraphValidationConfig>
  | RatingAddUpdateReq
  | ShortAnswerAddUpdateReq<AnyShortAnswerValidationConfig>
  | TickBoxGridAddUpdateReq
  | TimeAddUpdateReq
