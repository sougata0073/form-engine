import {Component, inject, input, signal} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {FormResponseSummaries} from '../../../../model/form/form-response-summaries';
import {Router} from '@angular/router';
import {
  CommonResponseQuestionResponse
} from '../../../../model/edit-form/responses/question/common-response-question-response';
import {InfiniteScrollList} from '../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {FormResponseService} from '../../../../service/form-response-service';

@Component({
  selector: 'app-edit-form-response-question-response-container',
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-response-container.html',
  styleUrl: './edit-form-response-question-response-container.scss',
})
export class EditFormResponseQuestionResponseContainer {

  formId = input.required<string>()
  response = input.required<CommonResponseQuestionResponse>()

  private router = inject(Router)
  private formResponseService = inject(FormResponseService)

  formResponseSummaries = signal<FormResponseSummaries | null>(null)

  protected isNextPageLoading = signal<boolean>(false)
  protected hasMoreItems: boolean = true
  protected pageNumber: number = 0
  protected readonly pageSize: number = 20

  protected isMenuOpened = signal<boolean>(false)

  onResponseIdClick(id: string) {
    this.router.navigate(['forms', this.formId(), 'edit', 'responses', 'individual'], {queryParams: {r: id}})
  }

  loadNextPage() {
    if (this.isNextPageLoading() || !this.hasMoreItems) return

    this.isNextPageLoading.set(true)

    this.formResponseService.getFormResponseSummaries(
      this.formId(),
      this.response().questionId,
      this.response().formResponsesIdentifier,
      this.pageNumber,
      this.pageSize,
      res => {
        this.isNextPageLoading.set(false)

        if (res.responses.length) {
          this.pageNumber++
          
          if (res.responses.length < this.pageSize) {
            this.hasMoreItems = false
          }

          this.formResponseSummaries.update(prev => {
            if (prev) {
              return {...prev, responses: [...prev.responses, ...res.responses]}
            }
            return res
          })
        } else {
          this.hasMoreItems = false
        }
      }
    )
  }
}
