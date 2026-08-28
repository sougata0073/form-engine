import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface LinearScaleResponseQuestionRes extends ResponseQuestion<LinearScaleResponseQuestionResResponse> {
}

export interface LinearScaleResponseQuestionResResponse extends CommonResponseQuestionResponse {
  scale: number | null
}

export interface LinearScaleResponseQuestionResSummary extends ResponseQuestionSummary {
  fromNumber: number,
  toNumber: number
}
