import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface ShortAnswerResponseQuestionRes extends ResponseQuestion<ShortAnswerResponseQuestionResResponse> {
}

export interface ShortAnswerResponseQuestionResResponse extends CommonResponseQuestionResponse {
  text: string | null
}

export interface ShortAnswerResponseQuestionResSummary extends ResponseQuestionSummary {

}
