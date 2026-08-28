import {ResponseSummary} from './response-summary';

export interface DateResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  year: number,
  month: number,
  dates: DateCountPair[]
}

export interface DateCountPair {
  date: string,
  count: string
}
