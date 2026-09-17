import {CheckboxAddUpdateReq} from '../model/edit-form/question/addreq/checkbox-add-req';
import {AnyCheckboxValidationConfig} from './any-checkbox-validation-config';
import {DateAddUpdateReq} from '../model/edit-form/question/addreq/date-add-req';
import {DateTimeAddUpdateReq} from '../model/edit-form/question/addreq/date-time-add-req';
import {DropdownAddUpdateReq} from '../model/edit-form/question/addreq/dropdown-add-req';
import {DurationAddUpdateReq} from '../model/edit-form/question/addreq/duration-add-req';
import {FileUploadAddUpdateReq} from '../model/edit-form/question/addreq/file-upload-add-req';
import {LinearScaleAddUpdateReq} from '../model/edit-form/question/addreq/linear-scale-add-req';
import {MultipleChoiceAddUpdateReq} from '../model/edit-form/question/addreq/multiple-choice-add-req';
import {MultipleChoiceGridAddUpdateReq} from '../model/edit-form/question/addreq/multiple-choice-grid-add-req';
import {ParagraphAddUpdateReq} from '../model/edit-form/question/addreq/paragraph-add-req';
import {AnyParagraphValidationConfig} from './any-paragraph-validation-config';
import {RatingAddUpdateReq} from '../model/edit-form/question/addreq/rating-add-req';
import {ShortAnswerAddUpdateReq} from '../model/edit-form/question/addreq/short-answer-add-req';
import {AnyShortAnswerValidationConfig} from './any-short-answer-validation-config';
import {TickBoxGridAddUpdateReq} from '../model/edit-form/question/addreq/tick-box-grid-add-req';
import {TimeAddUpdateReq} from '../model/edit-form/question/addreq/time-add-req';

export type AnyQuestionAddReq =
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
