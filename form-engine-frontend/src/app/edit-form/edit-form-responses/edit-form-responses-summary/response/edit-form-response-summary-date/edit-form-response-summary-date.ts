import {Component, OnInit, signal} from '@angular/core';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  DateResponseSummaryRes,
  Response
} from '../../../../../model/edit-form/responses/summary/date-response-summary-res';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {DatePipe} from '@angular/common';

interface DateNode {
  name: string;
  children?: DateNode[];
}

@Component({
  selector: 'app-edit-form-response-summary-date',
  imports: [
    InfiniteScrollList,
    DatePipe,
  ],
  templateUrl: './edit-form-response-summary-date.html',
  styleUrl: './edit-form-response-summary-date.scss',
})
export class EditFormResponseSummaryDate extends EditFormResponseSummaryComponent<DateResponseSummaryRes> implements OnInit {

  protected responses = signal<Response[]>([])

  protected isNextPageLoading = signal<boolean>(false)
  protected hasMoreItems: boolean = true
  protected pageNumber: number = 0
  protected readonly pageSize: number = 20

  ngOnInit() {
    this.listStyle.update(prev => {
      return {...prev, 'margin': '0 0 0 1rem'}
    })
    // this.responses.set(this.responseSummary().responses)
  }

  loadNextPage() {
    if (this.isNextPageLoading() || !this.hasMoreItems) return

    this.isNextPageLoading.set(true)

    this.formResponseService.getResponseSummary<DateResponseSummaryRes>(
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
