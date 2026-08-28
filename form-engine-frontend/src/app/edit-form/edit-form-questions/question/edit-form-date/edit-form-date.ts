import {Component, OnInit} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {DateRes} from '../../../../model/edit-form/question/response/date-res';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {OnlyDateAddUpdateReq} from '../../../../model/edit-form/question/request/date-add-update-req';

@Component({
  selector: 'app-edit-form-date',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatDatepickerToggle,
    MatDatepicker,
    MatDatepickerInput,
    MatFormFieldModule
  ],
  providers: [provideNativeDateAdapter()],
  templateUrl: './edit-form-date.html',
  styleUrl: './edit-form-date.scss',
})
export class EditFormDate extends EditFormQuestionComponent<DateRes, OnlyDateAddUpdateReq> implements OnInit {

  ngOnInit() {
    this.canSaveQuestion.emit(true)
    this.hasError.emit(false)
  }

  override getOnlyQuestionAddUpdateReq(): OnlyDateAddUpdateReq {
    return {}
  }

}
