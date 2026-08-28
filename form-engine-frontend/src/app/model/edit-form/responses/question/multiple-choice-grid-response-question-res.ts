import {CommonResponseQuestionResponse} from './common-response-question-response';
import {
  OnlyMultipleChoiceGridColumnRes,
  OnlyMultipleChoiceGridRowRes
} from '../../question/response/multiple-choice-grid-res';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface MultipleChoiceGridResponseQuestionRes extends ResponseQuestion<MultipleChoiceGridResponseQuestionResResponse> {
  rowId: string,
}

export interface MultipleChoiceGridResponseQuestionResResponse extends CommonResponseQuestionResponse {
  columnId: string | null
}

export interface MultipleChoiceGridResponseQuestionResSummary extends ResponseQuestionSummary {
  rows: OnlyMultipleChoiceGridRowRes[],
  columns: OnlyMultipleChoiceGridColumnRes[],
}
