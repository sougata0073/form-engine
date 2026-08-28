import {Component, OnInit, signal} from '@angular/core';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  ParagraphResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/paragraph-response-summary-res';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';

@Component({
  selector: 'app-edit-form-response-summary-paragraph',
  imports: [
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-summary-paragraph.html',
  styleUrl: './edit-form-response-summary-paragraph.scss',
})
export class EditFormResponseSummaryParagraph extends EditFormResponseSummaryComponent<ParagraphResponseSummaryRes> implements OnInit {

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

    this.formResponseService.getResponseSummary<ParagraphResponseSummaryRes>(
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
