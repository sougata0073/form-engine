import {Component} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  ShortAnswerResponseQuestionRes,
  ShortAnswerResponseQuestionResResponse,
  ShortAnswerResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/short-answer-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {ShortAnswerRes} from '../../../../../model/edit-form/question/response/short-answer-res';
import {AnyShortAnswerValidationConfig} from '../../../../../type/any-short-answer-validation-config';

@Component({
  selector: 'app-edit-form-response-question-short-answer',
  imports: [
    MatCard,
    MatCardContent,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-short-answer.html',
  styleUrl: './edit-form-response-question-short-answer.scss',
})
export class EditFormResponseQuestionShortAnswer extends EditFormResponseQuestionComponent<ShortAnswerResponseQuestionRes, ShortAnswerResponseQuestionResResponse, ShortAnswerRes<AnyShortAnswerValidationConfig>> {

}
