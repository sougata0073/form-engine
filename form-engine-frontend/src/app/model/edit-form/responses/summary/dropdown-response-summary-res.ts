import {ResponseSummary} from './response-summary';

export interface DropdownResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  optionId: string,
  option: string,
  responseCount: string
}
