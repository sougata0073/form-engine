import { OnlyCheckboxUpdateReq } from "../model/edit-form/question/updatereq/checkbox-update-req";
import { OnlyDateTimeUpdateReq } from "../model/edit-form/question/updatereq/date-time-update-req";
import { OnlyDateUpdateReq } from "../model/edit-form/question/updatereq/date-update-req";
import { OnlyDropdownUpdateReq } from "../model/edit-form/question/updatereq/dropdown-update-req";
import { OnlyDurationUpdateReq } from "../model/edit-form/question/updatereq/duration-update-req";
import { OnlyFileUploadUpdateReq } from "../model/edit-form/question/updatereq/file-upload-update-req";
import { OnlyLinearScaleUpdateReq } from "../model/edit-form/question/updatereq/linear-scale-update-req";
import { OnlyMultipleChoiceGridUpdateReq } from "../model/edit-form/question/updatereq/multiple-choice-grid-update-req";
import { OnlyMultipleChoiceUpdateReq } from "../model/edit-form/question/updatereq/multiple-choice-update-req";
import { OnlyParagraphUpdateReq } from "../model/edit-form/question/updatereq/paragraph-update-req";
import { OnlyRatingUpdateReq } from "../model/edit-form/question/updatereq/rating-update-req";
import { OnlyShortAnswerUpdateReq } from "../model/edit-form/question/updatereq/short-answer-update-req";
import { OnlyTickBoxGridUpdateReq } from "../model/edit-form/question/updatereq/tick-box-grid-update-req";
import { OnlyTimeUpdateReq } from "../model/edit-form/question/updatereq/time-update--req";

export type AnyOnlyQuestionUpdateReq =
    OnlyCheckboxUpdateReq
    | OnlyDateUpdateReq
    | OnlyDateTimeUpdateReq
    | OnlyDropdownUpdateReq
    | OnlyDurationUpdateReq
    | OnlyFileUploadUpdateReq
    | OnlyLinearScaleUpdateReq
    | OnlyMultipleChoiceUpdateReq
    | OnlyMultipleChoiceGridUpdateReq
    | OnlyParagraphUpdateReq
    | OnlyRatingUpdateReq
    | OnlyShortAnswerUpdateReq
    | OnlyTickBoxGridUpdateReq
    | OnlyTimeUpdateReq
