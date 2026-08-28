import {CheckboxResponseSummaryRes} from '../model/edit-form/responses/summary/checkbox-response-summary-res';
import {DateResponseSummaryRes} from '../model/edit-form/responses/summary/date-response-summary-res';
import {DropdownResponseSummaryRes} from '../model/edit-form/responses/summary/dropdown-response-summary-res';
import {DurationResponseSummaryRes} from '../model/edit-form/responses/summary/duration-response-summary-res';
import {FileUploadResponseSummaryRes} from '../model/edit-form/responses/summary/file-upload-response-summary-res';
import {LinearScaleResponseSummaryRes} from '../model/edit-form/responses/summary/linear-scale-response-summary-res';
import {
  MultipleChoiceResponseSummaryRes
} from '../model/edit-form/responses/summary/multiple-choice-response-summary-res';
import {
  MultipleChoiceGridResponseSummaryRes
} from '../model/edit-form/responses/summary/multiple-choice-grid-response-summary-res';
import {ParagraphResponseSummaryRes} from '../model/edit-form/responses/summary/paragraph-response-summary-res';
import {RatingResponseSummaryRes} from '../model/edit-form/responses/summary/rating-response-summary-res';
import {ShortAnswerResponseSummaryRes} from '../model/edit-form/responses/summary/short-answer-response-summary-res';
import {TickBoxGridResponseSummaryRes} from '../model/edit-form/responses/summary/tick-box-grid-response-summary-res';
import {TimeResponseSummaryRes} from '../model/edit-form/responses/summary/time-response-summary-res';
import {DateTimeResponseSummaryRes} from '../model/edit-form/responses/summary/date-time-response-summary-res';

export type AnyResponseSummaryRes =
  | CheckboxResponseSummaryRes
  | DateResponseSummaryRes
  | DateTimeResponseSummaryRes
  | DropdownResponseSummaryRes
  | DurationResponseSummaryRes
  | FileUploadResponseSummaryRes
  | LinearScaleResponseSummaryRes
  | MultipleChoiceResponseSummaryRes
  | MultipleChoiceGridResponseSummaryRes
  | ParagraphResponseSummaryRes
  | RatingResponseSummaryRes
  | ShortAnswerResponseSummaryRes
  | TickBoxGridResponseSummaryRes
  | TimeResponseSummaryRes;
