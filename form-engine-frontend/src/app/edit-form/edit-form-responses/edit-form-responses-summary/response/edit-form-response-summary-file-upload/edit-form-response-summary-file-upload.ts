import {Component, OnInit, signal} from '@angular/core';
import {MatTooltip} from '@angular/material/tooltip';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  FileUploadResponseSummaryRes,
  Response
} from '../../../../../model/edit-form/responses/summary/file-upload-response-summary-res';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';

@Component({
  selector: 'app-edit-form-response-summary-file-upload',
  imports: [
    MatTooltip,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-summary-file-upload.html',
  styleUrl: './edit-form-response-summary-file-upload.scss',
})
export class EditFormResponseSummaryFileUpload extends EditFormResponseSummaryComponent<FileUploadResponseSummaryRes> implements OnInit {

  protected responses = signal<Response[]>([])

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

    this.formResponseService.getResponseSummary<FileUploadResponseSummaryRes>(
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
