import {CommonResponseQuestionResponse} from './common-response-question-response';
import {OnlyCheckboxOptionRes} from '../../question/response/checkbox-res';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface CheckboxResponseQuestionRes extends ResponseQuestion<CheckboxResponseQuestionResResponse> {
}

export interface CheckboxResponseQuestionResResponse extends CommonResponseQuestionResponse {
  optionIds: string[] | null
}

export interface CheckboxResponseQuestionResSummary extends ResponseQuestionSummary {
  options: OnlyCheckboxOptionRes[]
}
