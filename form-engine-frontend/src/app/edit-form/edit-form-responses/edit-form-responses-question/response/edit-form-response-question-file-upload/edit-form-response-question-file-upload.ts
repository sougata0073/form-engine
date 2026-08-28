import {Component} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  FileUploadResponseQuestionRes,
  FileUploadResponseQuestionResResponse,
  FileUploadResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/file-upload-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatTooltip} from '@angular/material/tooltip';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {FileUploadRes} from '../../../../../model/edit-form/question/response/file-upload-res';

@Component({
  selector: 'app-edit-form-response-question-file-upload',
  imports: [
    MatCard,
    MatCardContent,
    MatTooltip,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-file-upload.html',
  styleUrl: './edit-form-response-question-file-upload.scss',
})
export class EditFormResponseQuestionFileUpload extends EditFormResponseQuestionComponent<FileUploadResponseQuestionRes, FileUploadResponseQuestionResResponse, FileUploadRes> {

}
