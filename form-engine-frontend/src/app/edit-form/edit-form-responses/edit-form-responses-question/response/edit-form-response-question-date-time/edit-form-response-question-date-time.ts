import {Component} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  DateTimeResponseQuestionRes,
  DateTimeResponseQuestionResResponse,
  DateTimeResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/date-time-response-question-res';
import {DatePipe} from "@angular/common";
import {MatCard, MatCardContent} from "@angular/material/card";
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {DateTimeRes} from '../../../../../model/edit-form/question/response/date-time-res';

@Component({
  selector: 'app-edit-form-response-question-date-time',
  imports: [
    DatePipe,
    MatCard,
    MatCardContent,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-date-time.html',
  styleUrl: './edit-form-response-question-date-time.scss',
})
export class EditFormResponseQuestionDateTime extends EditFormResponseQuestionComponent<DateTimeResponseQuestionRes, DateTimeResponseQuestionResResponse, DateTimeRes> {

}
