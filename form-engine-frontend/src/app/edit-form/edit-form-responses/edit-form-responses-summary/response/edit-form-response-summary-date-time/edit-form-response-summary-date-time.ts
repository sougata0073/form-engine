import {Component, OnInit, signal} from '@angular/core';
import {DatePipe} from '@angular/common';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  DateTimeResponseSummaryRes,
  Response
} from '../../../../../model/edit-form/responses/summary/date-time-response-summary-res';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';

@Component({
  selector: 'app-edit-form-response-summary-date-time',
  imports: [
    DatePipe,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-summary-date-time.html',
  styleUrl: './edit-form-response-summary-date-time.scss',
})
export class EditFormResponseSummaryDateTime extends EditFormResponseSummaryComponent<DateTimeResponseSummaryRes> implements OnInit {

  protected responses = signal<{ id: string, response: Response }[]>([])

  protected isNextPageLoading = signal<boolean>(false)
  protected hasMoreItems: boolean = true
  protected pageNumber: number = 0
  protected readonly pageSize: number = 50

  ngOnInit() {
    this.listStyle.update(prev => {
      return {...prev, 'margin': '0 0 0 1rem'}
    })
    // this.responses.set(this.responseSummary().responses.map(response => ({id: crypto.randomUUID(), response})))
  }

  loadNextPage() {
    if (this.isNextPageLoading() || !this.hasMoreItems) return

    this.isNextPageLoading.set(true)

    this.formResponseService.getResponseSummary<DateTimeResponseSummaryRes>(
      this.formId(), this.responseSummary().questionId, this.pageNumber, this.pageSize, res => {
        this.isNextPageLoading.set(false)

        if (res.responses.length) {
          this.pageNumber++

          if (res.responses.length < this.pageSize) {
            this.hasMoreItems = false
          }

          this.responses.update(prev => {
            return [...prev, ...res.responses.map(response => ({id: crypto.randomUUID(), response}))]
          })
        } else {
          this.hasMoreItems = false
        }
      }
    )
  }

}
