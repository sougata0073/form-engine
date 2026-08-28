import {ResponseSummary} from './response-summary';

export interface CheckboxResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  optionId: string,
  option: string,
  responseCount: string
}
