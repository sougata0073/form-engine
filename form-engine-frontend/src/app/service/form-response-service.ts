import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormResponseCount } from '../model/form/form-response-count';
import { QuestionSummariesRes } from '../model/question/question-summaries-res';
import { ResponseSummaryRes } from '../model/edit-form/responses/summary/response-summary-res';
import { AnyResponseQuestionSummary } from '../type/any-response-question-summary';
import { FormResponseSummaries } from '../model/form/form-response-summaries';
import { ResponseSummary } from '../model/edit-form/responses/summary/response-summary';
import { FormDetails } from '../model/form/form-details';
import { ResponseIndividualRes } from '../model/edit-form/responses/individual/response-individual-res';
import { SuccessMessage } from '../model/common/success-message';

@Injectable({
  providedIn: 'root',
})
export class FormResponseService {
  private http = inject(HttpClient);

  getQuestionSummaries(formId: string, onComplete: (res: QuestionSummariesRes) => void) {
    const url = `http://localhost:9092/api/v1/forms/${formId}/question-summaries`;

    this.http.get<QuestionSummariesRes>(url, { params: { formId: formId } }).subscribe((res) => {
      onComplete(res);
    });
  }

  getResponseSummaries(formId: string, onComplete: (res: ResponseSummaryRes) => void) {
    const url = `http://localhost:9094/api/v1/forms/${formId}/response-summaries`;

    this.http.get<ResponseSummaryRes>(url).subscribe((res) => {
      onComplete(res);
    });
  }

  getResponseSummary<T extends ResponseSummary>(
    formId: string,
    questionId: string,
    pageNumber: number,
    pageSize: number,
    onComplete: (res: T) => void,
  ) {
    const url = `http://localhost:9094/api/v1/forms/${formId}/questions/${questionId}/response-summary`;

    this.http
      .get<T>(url, {
        params: {
          page: pageNumber,
          size: pageSize,
        },
      })
      .subscribe((res) => {
        onComplete(res);
      });
  }

  getResponseByQuestion<T>(
    formId: string,
    questionId: string,
    pageNumber: number,
    pageSize: number,
    extraParams: Record<string, string>,
    onComplete: (res: T) => void,
  ) {
    const url = `http://localhost:9094/api/v1/forms/${formId}/questions/${questionId}/response`;

    this.http
      .get<T>(url, {
        params: {
          page: pageNumber,
          size: pageSize,
          ...extraParams,
        },
      })
      .subscribe((res) => {
        onComplete(res);
      });
  }

  getFormResponseSummaries(
    formId: string,
    questionId: string,
    formResponsesIdentifier: string,
    pageNumber: number,
    pageSize: number,
    onComplete: (res: FormResponseSummaries) => void,
  ) {
    const url = `http://localhost:9094/api/v1/forms/${formId}/questions/${questionId}/form-response-summaries`;

    this.http
      .get<FormResponseSummaries>(url, {
        params: {
          formResponsesIdentifier: formResponsesIdentifier,
          page: pageNumber,
          size: pageSize,
        },
      })
      .subscribe((res) => {
        onComplete(res);
      });
  }

  getIndividualFormResponseByFormResponseId(
    formId: string,
    formResponseId: string,
    onComplete: (res: ResponseIndividualRes) => void,
  ) {
    const url = `http://localhost:9094/api/v1/forms/${formId}/responses/${formResponseId}`;

    this.http.get<ResponseIndividualRes>(url).subscribe((res) => {
      onComplete(res);
    });
  }

  getIndividualFormResponseByFormResponsePage(
    formId: string,
    formResponsePage: number,
    onComplete: (res: ResponseIndividualRes) => void,
  ) {
    const url = `http://localhost:9094/api/v1/forms/${formId}/responses`;

    this.http
      .get<ResponseIndividualRes>(url, { params: { page: formResponsePage } })
      .subscribe((res) => {
        onComplete(res);
      });
  }

  deleteFormResponse(
    formId: string,
    responderId: string,
    formResponseId: string,
    onComplete: () => void,
  ) {
    const url = `http://localhost:9093/api/v1/forms/${formId}/responders/${responderId}/responses/${formResponseId}`;

    this.http.delete<SuccessMessage>(url).subscribe((res) => {
      onComplete();
    });
  }
}
