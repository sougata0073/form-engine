import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {DateTimeRes} from '../../../model/edit-form/question/response/date-time-res';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatError, MatFormField, MatHint, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from '@angular/material/timepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {OnlyDateTimeResponsePutReq} from '../../../model/view-form/request/date-time-response-put-req';

@Component({
  selector: 'app-view-form-date-time',
  imports: [
    FormsModule,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatFormField,
    MatInput,
    MatLabel,
    MatSuffix,
    MatTimepicker,
    MatTimepickerInput,
    MatTimepickerToggle,
    ReactiveFormsModule,
    MatHint,
    MatError
  ],
  providers: [provideNativeDateAdapter()],
  templateUrl: './view-form-date-time.html',
  styleUrl: './view-form-date-time.scss',
})
export class ViewFormDateTime extends ViewFormQuestionComponent<DateTimeRes, OnlyDateTimeResponsePutReq> {

  protected dateTime = signal<Date | null>(null)

  override formGroup = new FormGroup({
    date: new FormControl<Date | null>(null),
    time: new FormControl<Date | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.formGroup.valueChanges.subscribe(val => {
      const d = val.date
      const t = val.time

      if (d && t) {
        const dateTime = new Date()
        dateTime.setFullYear(d.getFullYear(), d.getMonth(), d.getDate())
        dateTime.setHours(t.getHours(), t.getMinutes(), t.getSeconds())

        this.dateTime.set(dateTime)
      }
    })
  }

  override getOnlyQuestionResponsePutReq(): OnlyDateTimeResponsePutReq | null {
    const value = this.dateTime()
    return value ? {dateTime: value} : null
  }

  override clearForm() {
    super.clearForm();
    this.dateTime.set(null)
  }

}
