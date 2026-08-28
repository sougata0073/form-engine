import {Component} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  ParagraphResponseQuestionRes,
  ParagraphResponseQuestionResResponse,
  ParagraphResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/paragraph-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {ParagraphRes} from '../../../../../model/edit-form/question/response/paragraph-res';
import {AnyParagraphValidationConfig} from '../../../../../type/any-paragraph-validation-config';

@Component({
  selector: 'app-edit-form-response-question-paragraph',
  imports: [
    MatCard,
    MatCardContent,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-paragraph.html',
  styleUrl: './edit-form-response-question-paragraph.scss',
})
export class EditFormResponseQuestionParagraph extends EditFormResponseQuestionComponent<ParagraphResponseQuestionRes, ParagraphResponseQuestionResResponse, ParagraphRes<AnyParagraphValidationConfig>> {

}
