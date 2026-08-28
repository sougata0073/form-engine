import {CommonResponseQuestionResponse} from './common-response-question-response';
import {OnlyTickBoxGridColumnRes, OnlyTickBoxGridRowRes} from '../../question/response/tick-box-grid-res';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface TickBoxGridResponseQuestionRes extends ResponseQuestion<TickBoxGridResponseQuestionResResponse> {
  rowId: string
}

export interface TickBoxGridResponseQuestionResResponse extends CommonResponseQuestionResponse {
  columnIds: string[] | null
}

export interface TickBoxGridResponseQuestionResSummary extends ResponseQuestionSummary {
  rows: OnlyTickBoxGridRowRes[],
  columns: OnlyTickBoxGridColumnRes[],
}
