import {ResponseSummary} from './response-summary';

export interface DateTimeResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  date: string,
  time: string,
  timeCount: string
}
