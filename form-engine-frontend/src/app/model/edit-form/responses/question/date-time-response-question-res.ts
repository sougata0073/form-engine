import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface DateTimeResponseQuestionRes extends ResponseQuestion<DateTimeResponseQuestionResResponse> {
}

export interface DateTimeResponseQuestionResResponse extends CommonResponseQuestionResponse {
  dateTime: string | null
}

export interface DateTimeResponseQuestionResSummary extends ResponseQuestionSummary {

}
