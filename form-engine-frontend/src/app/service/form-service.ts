import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormDetails } from '../model/form/form-details';
import { FormAddUpdateReq } from '../model/form/form-add-update-req';
import { CopyFormReq } from '../model/form/copy-form-req';
import { FormInfoRes } from '../model/form/form-info-res';

@Injectable({
  providedIn: 'root',
})
export class FormService {

  private http = inject(HttpClient)

  createForm(form: FormAddUpdateReq, onComplete?: (res: FormInfoRes) => void) {
    const url = 'http://localhost:9092/api/v1/forms'

    this.http.post<FormInfoRes>(url, form).subscribe(res => {
      onComplete?.(res)
    })

  }

  copyForm(formId: string, copyFormReq: CopyFormReq, onComplete?: (res: FormInfoRes) => void) {
    const url = `http://localhost:9092/api/v1/forms/${formId}/copy`

    this.http.post<FormInfoRes>(url, copyFormReq).subscribe(res => {
      onComplete?.(res)
    })

  }

  getFormDetails(formId: string, onComplete: (res: FormDetails) => void) {
    const url = `http://localhost:9092/api/v1/forms/${formId}/details`

    this.http.get<FormDetails>(url).subscribe(res => {
      onComplete(res)
    })
  }

}
