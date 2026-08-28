import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {
  ShortAnswerResponseIndividual
} from '../../../../../model/edit-form/responses/individual/short-answer-response-individual';
import {ShortAnswerRes} from '../../../../../model/edit-form/question/response/short-answer-res';
import {AnyShortAnswerValidationConfig} from '../../../../../type/any-short-answer-validation-config';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";

@Component({
  selector: 'app-edit-form-response-individual-short-answer',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-short-answer.html',
  styleUrl: './edit-form-response-individual-short-answer.scss',
})
export class EditFormResponseIndividualShortAnswer extends EditFormResponseIndividualComponent<ShortAnswerRes<AnyShortAnswerValidationConfig>, ShortAnswerResponseIndividual>{

}
