import {CheckboxResponseQuestionRes} from '../model/edit-form/responses/question/checkbox-response-question-res';
import {DateResponseQuestionRes} from '../model/edit-form/responses/question/date-response-question-res';
import {DateTimeResponseQuestionRes} from '../model/edit-form/responses/question/date-time-response-question-res';
import {DropdownResponseQuestionRes} from '../model/edit-form/responses/question/dropdown-response-question-res';
import {DurationResponseQuestionRes} from '../model/edit-form/responses/question/duration-response-question-res';
import {FileUploadResponseQuestionRes} from '../model/edit-form/responses/question/file-upload-response-question-res';
import {LinearScaleResponseQuestionRes} from '../model/edit-form/responses/question/linear-scale-response-question-res';
import {
  MultipleChoiceResponseQuestionRes
} from '../model/edit-form/responses/question/multiple-choice-response-question-res';
import {
  MultipleChoiceGridResponseQuestionRes
} from '../model/edit-form/responses/question/multiple-choice-grid-response-question-res';
import {ParagraphResponseQuestionRes} from '../model/edit-form/responses/question/paragraph-response-question-res';
import {RatingResponseQuestionRes} from '../model/edit-form/responses/question/rating-response-question-res';
import {ShortAnswerResponseQuestionRes} from '../model/edit-form/responses/question/short-answer-response-question-res';
import {
  TickBoxGridResponseQuestionRes
} from '../model/edit-form/responses/question/tick-box-grid-response-question-res';
import {TimeResponseQuestionRes} from '../model/edit-form/responses/question/time-response-question-res';

export type AnyResponseQuestionRes =
  | CheckboxResponseQuestionRes
  | DateResponseQuestionRes
  | DateTimeResponseQuestionRes
  | DropdownResponseQuestionRes
  | DurationResponseQuestionRes
  | FileUploadResponseQuestionRes
  | LinearScaleResponseQuestionRes
  | MultipleChoiceResponseQuestionRes
  | MultipleChoiceGridResponseQuestionRes
  | ParagraphResponseQuestionRes
  | RatingResponseQuestionRes
  | ShortAnswerResponseQuestionRes
  | TickBoxGridResponseQuestionRes
  | TimeResponseQuestionRes;
