import {Component, OnInit, signal} from '@angular/core';
import {DatePipe} from "@angular/common";
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  Response,
  TimeResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/time-response-summary-res';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';

@Component({
  selector: 'app-edit-form-response-summary-time',
  imports: [
    DatePipe,
    InfiniteScrollList,
  ],
  templateUrl: './edit-form-response-summary-time.html',
  styleUrl: './edit-form-response-summary-time.scss',
})
export class EditFormResponseSummaryTime extends EditFormResponseSummaryComponent<TimeResponseSummaryRes> implements OnInit {

  protected responses = signal<Response[]>([])

  protected isNextPageLoading = signal<boolean>(false)
  protected hasMoreItems: boolean = true
  protected pageNumber: number = 0
  protected readonly pageSize: number = 15

  ngOnInit() {
    this.listStyle.update(prev => {
      return {...prev, 'margin': '0 0 0 1rem'}
    })
    // this.responses.set(this.responseSummary().responses)
  }

  loadNextPage() {
    if (this.isNextPageLoading() || !this.hasMoreItems) return

    this.isNextPageLoading.set(true)

    this.formResponseService.getResponseSummary<TimeResponseSummaryRes>(
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
