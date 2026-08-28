import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {
  DurationResponseIndividual
} from '../../../../../model/edit-form/responses/individual/duration-response-individual';
import {DurationRes} from '../../../../../model/edit-form/question/response/duration-res';

@Component({
  selector: 'app-edit-form-response-individual-duration',
  imports: [],
  templateUrl: './edit-form-response-individual-duration.html',
  styleUrl: './edit-form-response-individual-duration.scss',
})
export class EditFormResponseIndividualDuration extends EditFormResponseIndividualComponent<DurationRes, DurationResponseIndividual> {

}
