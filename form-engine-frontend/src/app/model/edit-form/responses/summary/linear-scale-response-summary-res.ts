import {ResponseSummary} from './response-summary';

export interface LinearScaleResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  scale: number,
  responseCount: string
}
