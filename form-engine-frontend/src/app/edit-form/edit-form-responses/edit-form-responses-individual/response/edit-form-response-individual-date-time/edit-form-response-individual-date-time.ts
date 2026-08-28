import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {DateTimeRes} from '../../../../../model/edit-form/question/response/date-time-res';
import {
  DateTimeResponseIndividual
} from '../../../../../model/edit-form/responses/individual/date-time-response-individual';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-edit-form-response-individual-date-time',
    imports: [
        DatePipe
    ],
  templateUrl: './edit-form-response-individual-date-time.html',
  styleUrl: './edit-form-response-individual-date-time.scss',
})
export class EditFormResponseIndividualDateTime extends EditFormResponseIndividualComponent<DateTimeRes, DateTimeResponseIndividual> {

}
