import {Component, computed, inject, input, OnChanges, OnInit, signal, SimpleChanges} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {QuestionCard} from '../../../shared/question-card/question-card';
import {FormDetails} from '../../../model/form/form-details';
import {FormResponseService} from '../../../service/form-response-service';
import {ActivatedRoute, Router} from '@angular/router';
import {ResponseIndividualRes} from '../../../model/edit-form/responses/individual/response-individual-res';
import {
  EditFormIndividualQuestionWrapper
} from './edit-form-individual-question-wrapper/edit-form-individual-question-wrapper';
import {AnyResponseIndividual} from '../../../type/any-response-individual';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTooltip} from '@angular/material/tooltip';
import {MatIcon} from '@angular/material/icon';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatCard, MatCardContent} from '@angular/material/card';
import {FormResponseCount} from '../../../model/form/form-response-count';
import {MatDialog} from '@angular/material/dialog';
import {FunctionalDialog} from '../../../shared/functional-dialog/functional-dialog';
import { FormService } from '../../../service/form-service';
import { FormResponseServiceShared } from '../../../service/form-response-service-shared';

@Component({
  selector: 'app-edit-form-responses-individual',
  imports: [
    FormsModule,
    QuestionCard,
    ReactiveFormsModule,
    EditFormIndividualQuestionWrapper,
    MatProgressSpinner,
    MatPaginator,
    MatTooltip,
    MatIcon,
    MatIconButton,
    MatCardContent,
    MatCard
  ],
  templateUrl: './edit-form-responses-individual.html',
  styleUrl: './edit-form-responses-individual.scss',
})
export class EditFormResponsesIndividual implements OnInit {

  formResponseId = input<string | null>(null, {alias: 'r'})
  formResponsePage = input<string | null>(null, {alias: 'rp'})

  private formService = inject(FormService)
  private formResponseService = inject(FormResponseService)
  private formResponseServiceShared = inject(FormResponseServiceShared)
  private router = inject(Router)
  private activatedRoute = inject(ActivatedRoute)
  private dialog = inject(MatDialog)

  protected formId = signal<string | null>(null)
  protected formResponseCount = this.formResponseServiceShared.formResponseCount
  protected formRes = signal<FormDetails | null>(null)
  protected individualResponse = signal<ResponseIndividualRes | null>(null)

  protected individualResponsePaginatorIndex = signal<number>(0)

  protected response = computed(() => {
    const res = this.individualResponse()

    if (!res) {
      return new Map<string, AnyResponseIndividual>();
    }

    return new Map(
      res.responses.map(r => [r.questionId, r])
    )
  })

  ngOnInit() {

    this.activatedRoute.parent!.parent!.paramMap.subscribe(params => {
      this.formId.set(params.get('formId')!);

      this.formService.getFormDetails(this.formId()!, res => {
        this.formRes.set(res)
      })

      const urlFormResponseId = this.activatedRoute.snapshot.queryParams['r']
      let urlFormResponsePage = this.activatedRoute.snapshot.queryParams['rp']

      if (urlFormResponseId !== undefined) {
        this.loadResponse(urlFormResponseId, undefined)
      } else if (urlFormResponsePage !== undefined) {
        this.loadResponse(undefined, urlFormResponsePage)
      } else {
        this.loadResponse(undefined, 0)
      }

      this.formResponseServiceShared.getFormResponseCount(this.formId()!, res => {

      })
    })
  }

  protected handleIndividualResponsePaginatorPageEvent(e: PageEvent) {
    this.loadResponse(undefined, e.pageIndex)
  }

  protected onPrintResponseClick() {
    window.print()
  }

  protected onDeleteResponseClick() {
    if (this.formId() && this.individualResponse()) {
      const dialogRef = this.dialog.open(FunctionalDialog, {
        data: FunctionalDialog.configure(
          'Delete response',
          'Are you sure you want to delete this response? This action cannot be undone.',
          'CANCEL',
          () => dialogRef.close(),
          'YES',
          () => {
            dialogRef.close()

            const responderId = this.individualResponse()!.userId
            const formResponseId = this.individualResponse()!.formResponseId

            this.individualResponse.set(null)

            this.formResponseService.deleteFormResponse(
              this.formId()!,
              responderId,
              formResponseId,
              () => {
                this.formResponseServiceShared.getFormResponseCount(this.formId()!, res => {
                  if (this.individualResponsePaginatorIndex() >= Number(res.count) - 1) {
                    this.individualResponsePaginatorIndex.update(prev => prev - 1)
                  }
                  this.loadResponse(undefined, this.individualResponsePaginatorIndex(), () => {

                  })
                })
              }
            )

          }
        )
      })
    }
  }

  private loadResponse(formResponseId?: string, formResponsePage?: number, onComplete?: () => void) {
    this.individualResponse.set(null)

    if (formResponseId !== undefined) {

      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: {r: formResponseId}
      })
      this.formResponseService.getIndividualFormResponseByFormResponseId(this.formId()!, formResponseId, res => {
        this.individualResponse.set(res)
        this.individualResponsePaginatorIndex.set(Number(res.page))
        onComplete?.()
      })

    } else if (formResponsePage !== undefined) {

      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: {rp: formResponsePage}
      })
      this.individualResponsePaginatorIndex.set(formResponsePage)

      this.formResponseService.getIndividualFormResponseByFormResponsePage(this.formId()!, formResponsePage, res => {
        this.individualResponse.set(res)
        onComplete?.()
      })

    }
  }
}
