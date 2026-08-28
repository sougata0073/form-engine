import {ResponseSummary} from './response-summary';

export interface TimeResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  hour: number,
  times: TimeCountPair[]
}

export interface TimeCountPair {
  time: string,
  count: string
}
