import { CheckboxUpdateReq } from "../model/edit-form/question/updatereq/checkbox-update-req";
import { DateTimeUpdateReq } from "../model/edit-form/question/updatereq/date-time-update-req";
import { DateUpdateReq } from "../model/edit-form/question/updatereq/date-update-req";
import { DropdownUpdateReq } from "../model/edit-form/question/updatereq/dropdown-update-req";
import { DurationUpdateReq } from "../model/edit-form/question/updatereq/duration-update-req";
import { FileUploadUpdateReq } from "../model/edit-form/question/updatereq/file-upload-update-req";
import { LinearScaleUpdateReq } from "../model/edit-form/question/updatereq/linear-scale-update-req";
import { MultipleChoiceGridUpdateReq } from "../model/edit-form/question/updatereq/multiple-choice-grid-update-req";
import { MultipleChoiceUpdateReq } from "../model/edit-form/question/updatereq/multiple-choice-update-req";
import { ParagraphUpdateReq } from "../model/edit-form/question/updatereq/paragraph-update-req";
import { RatingUpdateReq } from "../model/edit-form/question/updatereq/rating-update-req";
import { ShortAnswerUpdateReq } from "../model/edit-form/question/updatereq/short-answer-update-req";
import { TickBoxGridUpdateReq } from "../model/edit-form/question/updatereq/tick-box-grid-update-req";
import { TimeUpdateReq } from "../model/edit-form/question/updatereq/time-update--req";

export type AnyQuestionUpdateReq =
    CheckboxUpdateReq
    | DateUpdateReq
    | DateTimeUpdateReq
    | DropdownUpdateReq
    | DurationUpdateReq
    | FileUploadUpdateReq
    | LinearScaleUpdateReq
    | MultipleChoiceUpdateReq
    | MultipleChoiceGridUpdateReq
    | ParagraphUpdateReq
    | RatingUpdateReq
    | ShortAnswerUpdateReq
    | TickBoxGridUpdateReq
    | TimeUpdateReq