import { Component, inject, OnInit, signal } from '@angular/core';
import { EditFormResponseSummaryWrapper } from './edit-form-response-summary-wrapper/edit-form-response-summary-wrapper';
import { FormResponseService } from '../../../service/form-response-service';
import { ActivatedRoute } from '@angular/router';
import { ResponseSummaryRes } from '../../../model/edit-form/responses/summary/response-summary-res';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { FormResponseServiceShared } from '../../../service/form-response-service-shared';

@Component({
  selector: 'app-edit-form-responses-summary',
  imports: [
    EditFormResponseSummaryWrapper,
    MatProgressSpinner
  ],
  templateUrl: './edit-form-responses-summary.html',
  styleUrl: './edit-form-responses-summary.scss',
})
export class EditFormResponsesSummary implements OnInit {

  formId = signal<string | null>(null)

  private activatedRoute = inject(ActivatedRoute)
  private responseService = inject(FormResponseService)
  private formResponseServiceShared = inject(FormResponseServiceShared)

  protected responseSummaries = signal<ResponseSummaryRes | null>(null)

  ngOnInit() {

    this.activatedRoute.parent!.parent!.paramMap.subscribe(params => {
      this.formId.set(params.get('formId')!);

      this.responseService.getResponseSummaries(this.formId()!, res => {
        this.responseSummaries.set(res)
      })

      this.formResponseServiceShared.getFormResponseCount(this.formId()!, res => {

      })
    })

  }

}
