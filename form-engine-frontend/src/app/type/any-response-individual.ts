import {CheckboxResponseIndividual} from '../model/edit-form/responses/individual/checkbox-response-individual';
import {DateResponseIndividual} from '../model/edit-form/responses/individual/date-response-individual';
import {DateTimeResponseIndividual} from '../model/edit-form/responses/individual/date-time-response-individual';
import {DropdownResponseIndividual} from '../model/edit-form/responses/individual/dropdown-response-individual';
import {DurationResponseIndividual} from '../model/edit-form/responses/individual/duration-response-individual';
import {FileUploadResponseIndividual} from '../model/edit-form/responses/individual/file-upload-response-individual';
import {LinearScaleResponseIndividual} from '../model/edit-form/responses/individual/linear-scale-response-individual';
import {
  MultipleChoiceGridResponseIndividual
} from '../model/edit-form/responses/individual/multiple-choice-grid-response-individual';
import {
  MultipleChoiceResponseIndividual
} from '../model/edit-form/responses/individual/multiple-choice-response-individual';
import {ParagraphResponseIndividual} from '../model/edit-form/responses/individual/paragraph-response-individual';
import {RatingResponseIndividual} from '../model/edit-form/responses/individual/rating-response-individual';
import {ShortAnswerResponseIndividual} from '../model/edit-form/responses/individual/short-answer-response-individual';
import {TickBoxGridResponseIndividual} from '../model/edit-form/responses/individual/tick-box-grid-response-individual';
import {TimeResponseIndividual} from '../model/edit-form/responses/individual/time-response-individual';

export type AnyResponseIndividual =
  CheckboxResponseIndividual
  | DateResponseIndividual
  | DateTimeResponseIndividual
  | DropdownResponseIndividual
  | DurationResponseIndividual
  | FileUploadResponseIndividual
  | LinearScaleResponseIndividual
  | MultipleChoiceGridResponseIndividual
  | MultipleChoiceResponseIndividual
  | ParagraphResponseIndividual
  | RatingResponseIndividual
  | ShortAnswerResponseIndividual
  | TickBoxGridResponseIndividual
  | TimeResponseIndividual
