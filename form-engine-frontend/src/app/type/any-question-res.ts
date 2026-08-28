import {ShortAnswerRes} from '../model/edit-form/question/response/short-answer-res';
import {AnyShortAnswerValidationConfig} from './any-short-answer-validation-config';
import {CheckboxRes} from '../model/edit-form/question/response/checkbox-res';
import {AnyCheckboxValidationConfig} from './any-checkbox-validation-config';
import {DateRes} from '../model/edit-form/question/response/date-res';
import {DateTimeRes} from '../model/edit-form/question/response/date-time-res';
import {DropdownRes} from '../model/edit-form/question/response/dropdown-res';
import {DurationRes} from '../model/edit-form/question/response/duration-res';
import {FileUploadRes} from '../model/edit-form/question/response/file-upload-res';
import {LinearScaleRes} from '../model/edit-form/question/response/linear-scale-res';
import {MultipleChoiceGridRes} from '../model/edit-form/question/response/multiple-choice-grid-res';
import {MultipleChoiceRes} from '../model/edit-form/question/response/multiple-choice-res';
import {ParagraphRes} from '../model/edit-form/question/response/paragraph-res';
import {AnyParagraphValidationConfig} from './any-paragraph-validation-config';
import {RatingRes} from '../model/edit-form/question/response/rating-res';
import {TickBoxGridRes} from '../model/edit-form/question/response/tick-box-grid-res';
import {TimeRes} from '../model/edit-form/question/response/time-res';

export type AnyQuestionRes =
  CheckboxRes<AnyCheckboxValidationConfig>
  | DateRes
  | DateTimeRes
  | DropdownRes
  | DurationRes
  | FileUploadRes
  | LinearScaleRes
  | MultipleChoiceGridRes
  | MultipleChoiceRes
  | ParagraphRes<AnyParagraphValidationConfig>
  | RatingRes
  | ShortAnswerRes<AnyShortAnswerValidationConfig>
  | TickBoxGridRes
  | TimeRes
