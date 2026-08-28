import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface TimeResponseQuestionRes extends ResponseQuestion<TimeResponseQuestionResResponse> {
}

export interface TimeResponseQuestionResResponse extends CommonResponseQuestionResponse {
  time: string | null
}

export interface TimeResponseQuestionResSummary extends ResponseQuestionSummary {

}
