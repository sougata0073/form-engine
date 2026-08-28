import {CommonResponseQuestionResponse} from './common-response-question-response';
import {OnlyDropdownOptionRes} from '../../question/response/dropdown-res';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface DropdownResponseQuestionRes extends ResponseQuestion<DropdownResponseQuestionResResponse> {
}

export interface DropdownResponseQuestionResResponse extends CommonResponseQuestionResponse {
  optionId: string | null
}

export interface DropdownResponseQuestionResSummary extends ResponseQuestionSummary {
  options: OnlyDropdownOptionRes[]
}
