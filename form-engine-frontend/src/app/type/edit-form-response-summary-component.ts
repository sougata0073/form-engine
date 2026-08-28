import {Directive, inject, input, signal} from '@angular/core';
import {ResponseSummary} from '../model/edit-form/responses/summary/response-summary';
import {FormResponseService} from '../service/form-response-service';

@Directive()
export class EditFormResponseSummaryComponent<R extends ResponseSummary> {

  formId = input.required<string>()
  responseSummary = input.required<R>();

  protected formResponseService = inject(FormResponseService)

  protected listStyle = signal<Record<string, string>>({
    'max-height': '520px'
  })
}
