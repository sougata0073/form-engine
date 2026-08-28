import {ResponseSummary} from './response-summary';

export interface FileUploadResponseSummaryRes extends ResponseSummary {
  responses: Response[]
}

export interface Response {
  fileName: string,
  fileUrl: string,
  fileMimeType: string
}
