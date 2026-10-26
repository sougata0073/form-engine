import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormDetails } from '../model/form/form-details';
import { CopyFormReq } from '../model/form/copy-form-req';
import { FormInfo } from '../model/form/form-info';

@Injectable({
  providedIn: 'root',
})
export class FormService {

  private http = inject(HttpClient)

  createForm(onComplete?: (res: FormInfo) => void) {
    const url = 'forms'

    this.http.post<FormInfo>(url, {}).subscribe(res => {
      onComplete?.(res)
    })

  }

  copyForm(formId: string, copyFormReq: CopyFormReq, onComplete?: (res: FormInfo) => void) {
    const url = `forms/${formId}/copy`

    this.http.post<FormInfo>(url, copyFormReq).subscribe(res => {
      onComplete?.(res)
    })

  }

  getFormDetails(formId: string, onComplete: (res: FormDetails) => void) {
    const url = `forms/${formId}/details`

    this.http.get<FormDetails>(url).subscribe(res => {
      onComplete(res)
    })
  }

}
