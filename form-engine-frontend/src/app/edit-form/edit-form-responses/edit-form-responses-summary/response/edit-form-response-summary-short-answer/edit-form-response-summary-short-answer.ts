import {Component, OnInit, signal} from '@angular/core';
import {
  ShortAnswerResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/short-answer-response-summary-res';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';

@Component({
  selector: 'app-edit-form-response-summary-short-answer',
  imports: [
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-summary-short-answer.html',
  styleUrl: './edit-form-response-summary-short-answer.scss',
})
export class EditFormResponseSummaryShortAnswer extends EditFormResponseSummaryComponent<ShortAnswerResponseSummaryRes> implements OnInit {

  protected responses = signal<string[]>([])

  protected isNextPageLoading = signal<boolean>(false)
  protected hasMoreItems: boolean = true
  protected pageNumber: number = 0
  protected readonly pageSize: number = 20

  ngOnInit() {
    // this.responses.set(this.responseSummary().responses)
  }

  loadNextPage() {
    if (this.isNextPageLoading() || !this.hasMoreItems) return

    this.isNextPageLoading.set(true)

    this.formResponseService.getResponseSummary<ShortAnswerResponseSummaryRes>(
      this.formId(), this.responseSummary().questionId, this.pageNumber, this.pageSize, res => {
        this.isNextPageLoading.set(false)

        if (res.responses.length) {
          this.pageNumber++

          if (res.responses.length < this.pageSize) {
            this.hasMoreItems = false
          }

          this.responses.update(prev => {
            return [...prev, ...res.responses]
          })
        } else {
          this.hasMoreItems = false
        }
      }
    )
  }
}
