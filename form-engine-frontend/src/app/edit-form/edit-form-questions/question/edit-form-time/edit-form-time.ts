import {Component, OnInit} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {TimeRes} from '../../../../model/edit-form/question/response/time-res';
import {MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from '@angular/material/timepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {OnlyTimeAddUpdateReq} from '../../../../model/edit-form/question/request/time-add-update-req';

@Component({
  selector: 'app-edit-form-time',
  imports: [
    MatFormField,
    MatLabel,
    MatInput,
    MatTimepickerInput,
    MatTimepickerToggle,
    MatSuffix,
    MatTimepicker
  ],
  templateUrl: './edit-form-time.html',
  styleUrl: './edit-form-time.scss',
  providers: [provideNativeDateAdapter()]
})
export class EditFormTime extends EditFormQuestionComponent<TimeRes, OnlyTimeAddUpdateReq> implements OnInit {

  ngOnInit() {
    this.canSaveQuestion.emit(true)
    this.hasError.emit(false)
  }

  override getOnlyQuestionAddUpdateReq(): OnlyTimeAddUpdateReq {
    return {}
  }

}
