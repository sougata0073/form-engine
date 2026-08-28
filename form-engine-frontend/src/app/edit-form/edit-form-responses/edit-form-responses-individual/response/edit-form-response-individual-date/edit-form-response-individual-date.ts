import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {DateRes} from '../../../../../model/edit-form/question/response/date-res';
import {DateResponseIndividual} from '../../../../../model/edit-form/responses/individual/date-response-individual';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-edit-form-response-individual-date',
    imports: [
        DatePipe
    ],
  templateUrl: './edit-form-response-individual-date.html',
  styleUrl: './edit-form-response-individual-date.scss',
})
export class EditFormResponseIndividualDate extends EditFormResponseIndividualComponent<DateRes, DateResponseIndividual> {

}
