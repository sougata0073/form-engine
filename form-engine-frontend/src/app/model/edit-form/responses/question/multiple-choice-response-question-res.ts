import {CommonResponseQuestionResponse} from './common-response-question-response';
import {OnlyMultipleChoiceOptionRes} from '../../question/response/multiple-choice-res';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface MultipleChoiceResponseQuestionRes extends ResponseQuestion<MultipleChoiceResponseQuestionResResponse> {

}

export interface MultipleChoiceResponseQuestionResResponse extends CommonResponseQuestionResponse {
  optionId: string | null
}

export interface MultipleChoiceResponseQuestionResSummary extends ResponseQuestionSummary {
  options: OnlyMultipleChoiceOptionRes[],
}
