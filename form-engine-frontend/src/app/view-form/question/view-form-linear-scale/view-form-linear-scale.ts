import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {LinearScaleRes} from '../../../model/edit-form/question/response/linear-scale-res';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {ArrayUtil} from '../../../util/array-util';
import {MatButton} from '@angular/material/button';
import {OnlyLinearScaleResponsePutReq} from '../../../model/view-form/request/linear-scale-response-put-req';

@Component({
  selector: 'app-view-form-linear-scale',
  imports: [
    ReactiveFormsModule,
    MatRadioGroup,
    MatRadioButton,
    MatButton
  ],
  templateUrl: './view-form-linear-scale.html',
  styleUrl: './view-form-linear-scale.scss',
})
export class ViewFormLinearScale extends ViewFormQuestionComponent<LinearScaleRes, OnlyLinearScaleResponsePutReq> {

  protected radioButtons = signal<number[]>([])

  override formGroup = new FormGroup({
    linearScale: new FormControl<number | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.radioButtons.set(ArrayUtil.fillByNumbers(this.question().fromNumber, this.question().toNumber))
  }

  override getOnlyQuestionResponsePutReq(): OnlyLinearScaleResponsePutReq | null {
    const value = this.formGroup.value.linearScale
    return value === null || value === undefined ? null : {
      scale: value
    }
  }

  protected onRadioButtonCLick(value: number) {
    if (!this.question().required && this.formGroup.value.linearScale === value) {
      this.formGroup.controls.linearScale.patchValue(null)
      this.hasError.emit(this.formGroup.invalid)
    }
  }
}
