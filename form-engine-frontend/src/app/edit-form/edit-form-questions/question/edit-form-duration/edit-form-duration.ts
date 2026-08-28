import {Component, OnInit} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {DurationRes} from '../../../../model/edit-form/question/response/duration-res';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {ReactiveFormsModule} from '@angular/forms';
import {OnlyDurationAddUpdateReq} from '../../../../model/edit-form/question/request/duration-add-update-req';

@Component({
  selector: 'app-edit-form-duration',
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-duration.html',
  styleUrl: './edit-form-duration.scss',
})
export class EditFormDuration extends EditFormQuestionComponent<DurationRes, OnlyDurationAddUpdateReq> implements OnInit {

  ngOnInit() {
    this.canSaveQuestion.emit(true)
    this.hasError.emit(false)
  }

  override getOnlyQuestionAddUpdateReq(): OnlyDurationAddUpdateReq {
    return {}
  }

}
