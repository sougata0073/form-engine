import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface DurationResponseQuestionRes extends ResponseQuestion<DurationResponseQuestionResResponse> {
}

export interface DurationResponseQuestionResResponse extends CommonResponseQuestionResponse {
  hours: number | null,
  minutes: number | null,
  seconds: number | null
}

export interface DurationResponseQuestionResSummary extends ResponseQuestionSummary {

}
