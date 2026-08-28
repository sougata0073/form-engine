import {Component} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  DurationResponseQuestionRes,
  DurationResponseQuestionResResponse,
  DurationResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/duration-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {DurationRes} from '../../../../../model/edit-form/question/response/duration-res';

@Component({
  selector: 'app-edit-form-response-question-duration',
  imports: [
    MatCard,
    MatCardContent,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-duration.html',
  styleUrl: './edit-form-response-question-duration.scss',
})
export class EditFormResponseQuestionDuration extends EditFormResponseQuestionComponent<DurationResponseQuestionRes, DurationResponseQuestionResResponse, DurationRes> {

}
