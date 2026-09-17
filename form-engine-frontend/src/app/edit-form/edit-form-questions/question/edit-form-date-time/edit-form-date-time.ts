import {Component, OnInit} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {DateTimeRes} from '../../../../model/edit-form/question/response/date-time-res';
import {MatFormField, MatInput, MatInputModule, MatLabel, MatSuffix} from '@angular/material/input';
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle
} from '@angular/material/datepicker';
import {
  MatTimepicker,
  MatTimepickerInput,
  MatTimepickerModule,
  MatTimepickerToggle
} from '@angular/material/timepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {OnlyDateTimeAddUpdateReq} from '../../../../model/edit-form/question/addreq/date-time-add-req';
import { OnlyDateTimeUpdateReq } from '../../../../model/edit-form/question/updatereq/date-time-update-req';

@Component({
  selector: 'app-edit-form-date-time',
  imports: [
    MatFormField,
    MatLabel,
    MatInput,
    MatDatepickerInput,
    MatDatepicker,
    MatDatepickerToggle,
    MatSuffix,
    MatTimepickerInput,
    MatTimepicker,
    MatTimepickerToggle,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTimepickerModule,
    MatDatepickerModule
  ],
  providers: [provideNativeDateAdapter()],
  templateUrl: './edit-form-date-time.html',
  styleUrl: './edit-form-date-time.scss',
})
export class EditFormDateTime extends EditFormQuestionComponent<DateTimeRes, OnlyDateTimeUpdateReq> implements OnInit {

  ngOnInit() {
    this.canSaveQuestion.emit(true)
    this.hasError.emit(false)
  }

}
