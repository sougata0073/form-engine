import {ResponseSummary} from './response-summary';

export interface DurationResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  hours: number,
  durations: DurationCountPair[]
}

export interface DurationCountPair {
  minutes: number,
  seconds: number,
  count: string
}
