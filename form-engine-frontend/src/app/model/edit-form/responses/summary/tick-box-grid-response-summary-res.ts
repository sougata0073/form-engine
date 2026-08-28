import {ResponseSummary} from './response-summary';

export interface TickBoxGridResponseSummaryRes extends ResponseSummary {
  responses: RowResponse[]
}

export interface RowResponse {
  rowId: string,
  row: string,
  responses: ColumnResponse[]
}

export interface ColumnResponse {
  columnId: string,
  column: string,
  responseCount: string
}
