import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormSummaryRes} from '../model/form/form-summary-res';
import {TemplateSummaryRes} from '../model/template/template-summary-res';
import {TemplateToFormBuildRes} from '../model/template/template-to-form-build-res';
import {FormRenameReq} from '../model/form/form-rename-req';
import {SuccessMessage} from '../model/common/success-message';
import {TemplateSummariesRes} from '../model/template/template-summaries-res';
import {FormSummariesRes} from '../model/form/form-summaries-res';

@Injectable({
  providedIn: 'root',
})
export class HomeService {

  private http = inject(HttpClient)

  getRecentForms() {
    const url = 'forms/recent'

    return this.http.get<FormSummariesRes>(url)
  }

  getTemplates() {
    const url = 'templates'

    return this.http.get<TemplateSummariesRes>(url)
  }

  buildFormFromTemplate(templateId: string) {
    const url = `templates/${templateId}/build-form`

    return this.http.post<TemplateToFormBuildRes>(url, {})
  }

  renameForm(formId: string, req: FormRenameReq) {
    const url = `forms/${formId}/rename`

    return this.http.patch<SuccessMessage>(url, req)
  }

  deleteForm(formId: string) {
    const url = `forms/${formId}`

    return this.http.delete<SuccessMessage>(url)
  }

}
