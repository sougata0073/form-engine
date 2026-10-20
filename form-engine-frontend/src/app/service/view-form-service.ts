import { inject, Injectable, signal } from '@angular/core';
import { FormDetails } from '../model/form/form-details';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ViewFormErrorRes } from '../model/form/view-form-error-res';
import { FormResponsePutReq } from '../model/form/form-response-put-req';
import { FormResponsePutRes } from '../model/form/form-response-put-res';
import { ProblemDetail } from '../type/problem-detail';

@Injectable({
  providedIn: 'root',
})
export class ViewFormService {
  private http = inject(HttpClient);

  loadFormRes(
    formId: string,
    onComplete?: (res: FormDetails) => void,
    onError?: (error: ViewFormErrorRes) => void,
  ) {

    const url = `forms/${formId}/view`;

    this.http.get<FormDetails>(url, { observe: 'response' }).subscribe({
      next: (res) => {
        if (res.ok && res.body) {
          onComplete?.(res.body);
        }
      },
      complete: () => {},
      error: (err: HttpErrorResponse) => {
        onError?.(err.error);
      },
    });
  }

  submitResponse(
    formId: string,
    req: FormResponsePutReq,
    onComplete?: () => void,
    onError?: (pb: ProblemDetail) => void,
  ) {
    const url = `form-data/${formId}/responses`;

    this.http.post<FormResponsePutRes>(url, req).subscribe({
      next: (res) => {},
      complete: () => {
        onComplete?.();
      },
      error: (err: HttpErrorResponse) => {
        onError?.(err.error);
      },
    });
  }
}
