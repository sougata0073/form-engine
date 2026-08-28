import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {ParagraphRes} from '../../../../../model/edit-form/question/response/paragraph-res';
import {AnyParagraphValidationConfig} from '../../../../../type/any-paragraph-validation-config';
import {
  ParagraphResponseIndividual
} from '../../../../../model/edit-form/responses/individual/paragraph-response-individual';
import {CdkTextareaAutosize} from "@angular/cdk/text-field";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";

@Component({
  selector: 'app-edit-form-response-individual-paragraph',
  imports: [
    CdkTextareaAutosize,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-paragraph.html',
  styleUrl: './edit-form-response-individual-paragraph.scss',
})
export class EditFormResponseIndividualParagraph extends EditFormResponseIndividualComponent<ParagraphRes<AnyParagraphValidationConfig>, ParagraphResponseIndividual> {

}
