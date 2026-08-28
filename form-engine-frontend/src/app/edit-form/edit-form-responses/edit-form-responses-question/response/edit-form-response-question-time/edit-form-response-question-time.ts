import {Component} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  TimeResponseQuestionRes,
  TimeResponseQuestionResResponse,
  TimeResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/time-response-question-res';
import {DatePipe} from "@angular/common";
import {MatCard, MatCardContent} from "@angular/material/card";
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {TimeRes} from '../../../../../model/edit-form/question/response/time-res';

@Component({
  selector: 'app-edit-form-response-question-time',
  imports: [
    DatePipe,
    MatCard,
    MatCardContent,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-time.html',
  styleUrl: './edit-form-response-question-time.scss',
})
export class EditFormResponseQuestionTime extends EditFormResponseQuestionComponent<TimeResponseQuestionRes, TimeResponseQuestionResResponse, TimeRes> {

}
