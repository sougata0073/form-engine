import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {ResponseQuestionSummary} from './response-question-summary';

export interface FileUploadResponseQuestionRes extends ResponseQuestion<FileUploadResponseQuestionResResponse> {
}

export interface FileUploadResponseQuestionResResponse extends CommonResponseQuestionResponse {
  fileName: string | null,
  fileUrl: string | null,
  fileMimeType: string | null
}

export interface FileUploadResponseQuestionResSummary extends ResponseQuestionSummary {

}
