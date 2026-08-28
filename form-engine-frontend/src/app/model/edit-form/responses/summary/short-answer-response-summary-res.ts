import {ResponseSummary} from './response-summary';

export interface ShortAnswerResponseSummaryRes extends ResponseSummary {
  responses: string[]
}
