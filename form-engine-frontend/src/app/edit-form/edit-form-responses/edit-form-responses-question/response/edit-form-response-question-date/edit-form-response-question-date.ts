import {Component} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  DateResponseQuestionRes,
  DateResponseQuestionResResponse,
  DateResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/date-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {DatePipe} from '@angular/common';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {DateRes} from '../../../../../model/edit-form/question/response/date-res';

@Component({
  selector: 'app-edit-form-response-question-date',
  imports: [
    MatCard,
    MatCardContent,
    DatePipe,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-date.html',
  styleUrl: './edit-form-response-question-date.scss',
})
export class EditFormResponseQuestionDate extends EditFormResponseQuestionComponent<DateResponseQuestionRes, DateResponseQuestionResResponse, DateRes> {

}
