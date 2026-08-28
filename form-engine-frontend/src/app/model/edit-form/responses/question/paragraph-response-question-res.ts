import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface ParagraphResponseQuestionRes extends ResponseQuestion<ParagraphResponseQuestionResResponse> {
}

export interface ParagraphResponseQuestionResResponse extends CommonResponseQuestionResponse {
  text: string | null
}

export interface ParagraphResponseQuestionResSummary extends ResponseQuestionSummary {

}
