import {CheckboxResponsePutReq} from '../model/view-form/request/checkbox-response-put-req';
import {DateResponsePutReq} from '../model/view-form/request/date-response-put-req';
import {DateTimeResponsePutReq} from '../model/view-form/request/date-time-response-put-req';
import {DropdownResponsePutReq} from '../model/view-form/request/dropdown-response-put-req';
import {DurationResponsePutReq} from '../model/view-form/request/duration-response-put-req';
import {FileUploadResponsePutReq} from '../model/view-form/request/file-upload-response-put-req';
import {LinearScaleResponsePutReq} from '../model/view-form/request/linear-scale-response-put-req';
import {MultipleChoiceResponsePutReq} from '../model/view-form/request/multiple-choice-response-put-req';
import {MultipleChoiceGridResponsePutReq} from '../model/view-form/request/multiple-choice-grid-response-put-req';
import {ParagraphResponsePutReq} from '../model/view-form/request/paragraph-response-put-req';
import {RatingResponsePutReq} from '../model/view-form/request/rating-response-put-req';
import {ShortAnswerResponsePutReq} from '../model/view-form/request/short-answer-response-put-req';
import {TickBoxGridResponsePutReq} from '../model/view-form/request/tick-box-grid-response-put-req';
import {TimeResponsePutReq} from '../model/view-form/request/time-response-put-req';

export type AnyQuestionResponsePutReq =
  CheckboxResponsePutReq
  | DateResponsePutReq
  | DateTimeResponsePutReq
  | DropdownResponsePutReq
  | DurationResponsePutReq
  | FileUploadResponsePutReq
  | LinearScaleResponsePutReq
  | MultipleChoiceResponsePutReq
  | MultipleChoiceGridResponsePutReq
  | ParagraphResponsePutReq
  | RatingResponsePutReq
  | ShortAnswerResponsePutReq
  | TickBoxGridResponsePutReq
  | TimeResponsePutReq
