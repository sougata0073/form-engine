import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {
  FileUploadResponseIndividual
} from '../../../../../model/edit-form/responses/individual/file-upload-response-individual';
import {FileUploadRes} from "../../../../../model/edit-form/question/response/file-upload-res";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-edit-form-response-individual-file-upload',
  imports: [
    FormsModule,
    MatButton,
    MatIcon,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-file-upload.html',
  styleUrl: './edit-form-response-individual-file-upload.scss',
})
export class EditFormResponseIndividualFileUpload extends EditFormResponseIndividualComponent<FileUploadRes, FileUploadResponseIndividual> {

}
