import {CheckboxResponseQuestionResSummary} from '../model/edit-form/responses/question/checkbox-response-question-res';
import {DateResponseQuestionResSummary} from '../model/edit-form/responses/question/date-response-question-res';
import {
  DateTimeResponseQuestionResSummary
} from '../model/edit-form/responses/question/date-time-response-question-res';
import {DropdownResponseQuestionResSummary} from '../model/edit-form/responses/question/dropdown-response-question-res';
import {DurationResponseQuestionResSummary} from '../model/edit-form/responses/question/duration-response-question-res';
import {
  FileUploadResponseQuestionResSummary
} from '../model/edit-form/responses/question/file-upload-response-question-res';
import {
  LinearScaleResponseQuestionResSummary
} from '../model/edit-form/responses/question/linear-scale-response-question-res';
import {
  MultipleChoiceGridResponseQuestionResSummary
} from '../model/edit-form/responses/question/multiple-choice-grid-response-question-res';
import {
  MultipleChoiceResponseQuestionResSummary
} from '../model/edit-form/responses/question/multiple-choice-response-question-res';
import {
  ParagraphResponseQuestionResSummary
} from '../model/edit-form/responses/question/paragraph-response-question-res';
import {RatingResponseQuestionResSummary} from '../model/edit-form/responses/question/rating-response-question-res';
import {
  ShortAnswerResponseQuestionResSummary
} from '../model/edit-form/responses/question/short-answer-response-question-res';
import {
  TickBoxGridResponseQuestionResSummary
} from '../model/edit-form/responses/question/tick-box-grid-response-question-res';
import {TimeResponseQuestionResSummary} from '../model/edit-form/responses/question/time-response-question-res';

export type AnyResponseQuestionSummary =
  CheckboxResponseQuestionResSummary |
  DateResponseQuestionResSummary |
  DateTimeResponseQuestionResSummary |
  DropdownResponseQuestionResSummary |
  DurationResponseQuestionResSummary |
  FileUploadResponseQuestionResSummary |
  LinearScaleResponseQuestionResSummary |
  MultipleChoiceGridResponseQuestionResSummary |
  MultipleChoiceResponseQuestionResSummary |
  ParagraphResponseQuestionResSummary |
  RatingResponseQuestionResSummary |
  ShortAnswerResponseQuestionResSummary |
  TickBoxGridResponseQuestionResSummary |
  TimeResponseQuestionResSummary
