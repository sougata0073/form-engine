import {Component, signal} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  TickBoxGridResponseQuestionRes,
  TickBoxGridResponseQuestionResResponse,
  TickBoxGridResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/tick-box-grid-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatCheckbox} from '@angular/material/checkbox';
import {
  OnlyTickBoxGridColumnRes,
  OnlyTickBoxGridRowRes, TickBoxGridRes
} from '../../../../../model/edit-form/question/response/tick-box-grid-res';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';

@Component({
  selector: 'app-edit-form-response-question-tick-box-grid',
  imports: [
    MatCard,
    MatCardContent,
    MatCheckbox,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-tick-box-grid.html',
  styleUrl: './edit-form-response-question-tick-box-grid.scss',
})
export class EditFormResponseQuestionTickBoxGrid extends EditFormResponseQuestionComponent<TickBoxGridResponseQuestionRes, TickBoxGridResponseQuestionResResponse, TickBoxGridRes> {

  protected rows = signal<OnlyTickBoxGridRowRes[]>([])
  protected columns = signal<OnlyTickBoxGridColumnRes[]>([])
  protected selectedRowId = signal<string | null>(null)

  override ngOnInit() {
    this.rows.set(this.questionDetails().rows)
    this.columns.set(this.questionDetails().columns)

    const firstRow = this.rows().at(0)!
    this.selectedRowId.set(firstRow.id)
    this.extraParam.set({rowId: this.selectedRowId()!})
  }

  protected selectRow(rowId: string) {
    this.selectedRowId.set(rowId)

    this.isNextPageLoading.set(false)
    this.hasMoreItems = true
    this.pageNumber = 0

    this.extraParam.set({rowId: this.selectedRowId()!})

    this.loadNextPage(true)
  }

  protected getColumnFromId(id: string) {
    return this.questionDetails().columns.find(col => col.id === id);
  }
}
