import {Component, signal} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  DropdownResponseQuestionRes,
  DropdownResponseQuestionResResponse,
  DropdownResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/dropdown-response-question-res';
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {DropdownRes} from '../../../../../model/edit-form/question/response/dropdown-res';

@Component({
  selector: 'app-edit-form-response-question-dropdown',
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatIcon,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-dropdown.html',
  styleUrl: './edit-form-response-question-dropdown.scss',
})
export class EditFormResponseQuestionDropdown extends EditFormResponseQuestionComponent<DropdownResponseQuestionRes, DropdownResponseQuestionResResponse, DropdownRes> {

  protected isOptionsVisible = signal<boolean>(false)

  getOptionFromId(id: string) {
    return this.questionDetails().options.find(op => op.id === id);
  }

}
