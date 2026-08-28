import {Component} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {TimeRes} from '../../../model/edit-form/question/response/time-res';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from "@angular/material/input";
import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from "@angular/material/timepicker";
import {provideNativeDateAdapter} from '@angular/material/core';
import {OnlyTimeResponsePutReq} from '../../../model/view-form/request/time-response-put-req';

@Component({
  selector: 'app-view-form-time',
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatSuffix,
    MatTimepicker,
    MatTimepickerInput,
    MatTimepickerToggle,
    ReactiveFormsModule,
    MatError
  ],
  templateUrl: './view-form-time.html',
  styleUrl: './view-form-time.scss',
  providers: [provideNativeDateAdapter()]
})
export class ViewFormTime extends ViewFormQuestionComponent<TimeRes, OnlyTimeResponsePutReq> {

  override formGroup = new FormGroup({
    time: new FormControl<Date | null>(null)
  })

  override getOnlyQuestionResponsePutReq(): OnlyTimeResponsePutReq | null {
    const value = this.formGroup.value.time
    return !value ? null : {time: value}
  }
}
