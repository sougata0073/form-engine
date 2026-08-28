import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface DateResponseQuestionRes extends ResponseQuestion<DateResponseQuestionResResponse> {
}

export interface DateResponseQuestionResResponse extends CommonResponseQuestionResponse {
  date: string | null
}

export interface DateResponseQuestionResSummary extends ResponseQuestionSummary {
}
