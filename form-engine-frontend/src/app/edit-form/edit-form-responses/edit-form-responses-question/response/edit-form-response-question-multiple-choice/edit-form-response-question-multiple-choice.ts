import {Component, signal} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  MultipleChoiceResponseQuestionRes,
  MultipleChoiceResponseQuestionResResponse,
  MultipleChoiceResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/multiple-choice-response-question-res';
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {MatRadioButton} from '@angular/material/radio';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {MultipleChoiceRes} from '../../../../../model/edit-form/question/response/multiple-choice-res';

@Component({
  selector: 'app-edit-form-response-question-multiple-choice',
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatIcon,
    MatRadioButton,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-multiple-choice.html',
  styleUrl: './edit-form-response-question-multiple-choice.scss',
})
export class EditFormResponseQuestionMultipleChoice extends EditFormResponseQuestionComponent<MultipleChoiceResponseQuestionRes, MultipleChoiceResponseQuestionResResponse, MultipleChoiceRes> {

  protected isOptionsVisible = signal<boolean>(false)

  getOptionFromId(id: string) {
    return this.questionDetails().options.find(op => op.id === id);
  }

}
