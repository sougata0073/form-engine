import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {TimeResponseIndividual} from '../../../../../model/edit-form/responses/individual/time-response-individual';
import {TimeRes} from '../../../../../model/edit-form/question/response/time-res';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-edit-form-response-individual-time',
    imports: [
        DatePipe
    ],
  templateUrl: './edit-form-response-individual-time.html',
  styleUrl: './edit-form-response-individual-time.scss',
})
export class EditFormResponseIndividualTime extends EditFormResponseIndividualComponent<TimeRes, TimeResponseIndividual>{

}
