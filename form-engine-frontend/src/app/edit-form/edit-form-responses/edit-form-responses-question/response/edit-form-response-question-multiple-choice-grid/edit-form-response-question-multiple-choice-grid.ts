import {Component, signal} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  MultipleChoiceGridResponseQuestionRes,
  MultipleChoiceGridResponseQuestionResResponse,
  MultipleChoiceGridResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/multiple-choice-grid-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {
  MultipleChoiceGridRes,
  OnlyMultipleChoiceGridColumnRes,
  OnlyMultipleChoiceGridRowRes
} from '../../../../../model/edit-form/question/response/multiple-choice-grid-res';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';

@Component({
  selector: 'app-edit-form-response-question-multiple-choice-grid',
  imports: [
    MatCard,
    MatCardContent,
    MatRadioButton,
    MatRadioGroup,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-multiple-choice-grid.html',
  styleUrl: './edit-form-response-question-multiple-choice-grid.scss',
})
export class EditFormResponseQuestionMultipleChoiceGrid extends EditFormResponseQuestionComponent<MultipleChoiceGridResponseQuestionRes, MultipleChoiceGridResponseQuestionResResponse, MultipleChoiceGridRes> {

  protected rows = signal<OnlyMultipleChoiceGridRowRes[]>([])
  protected columns = signal<OnlyMultipleChoiceGridColumnRes[]>([])
  protected selectedRowId = signal<string | null>(null)

  override ngOnInit() {
    this.rows.set(this.questionDetails().rows)
    this.columns.set(this.questionDetails().columns)

    const firstRow = this.rows().at(0)!
    this.selectedRowId.set(firstRow.id)
    this.extraParam.set({rowId: firstRow.id})
  }

  protected selectRow(rowId: string) {
    this.selectedRowId.set(rowId)

    this.isNextPageLoading.set(false)
    this.hasMoreItems = true
    this.pageNumber = 0

    this.extraParam.set({rowId: rowId})

    this.loadNextPage(true)
  }

  protected getColumnFromId(id: string) {
    return this.questionDetails().columns.find(col => col.id === id);
  }
}
