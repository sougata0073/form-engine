import { Component, signal } from '@angular/core';
import { ViewFormQuestionComponent } from '../../../type/view-form-question-component';
import { LinearScaleRes } from '../../../model/edit-form/question/response/linear-scale-res';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatRadioButton, MatRadioGroup } from '@angular/material/radio';
import { ArrayUtil } from '../../../util/array-util';
import { MatButton } from '@angular/material/button';
import { OnlyLinearScaleResponsePutReq } from '../../../model/view-form/request/linear-scale-response-put-req';

@Component({
  selector: 'app-view-form-linear-scale',
  imports: [ReactiveFormsModule, MatRadioGroup, MatRadioButton, MatButton],
  templateUrl: './view-form-linear-scale.html',
  styleUrl: './view-form-linear-scale.scss',
})
export class ViewFormLinearScale extends ViewFormQuestionComponent<
  LinearScaleRes,
  OnlyLinearScaleResponsePutReq
> {
  protected radioButtons = signal<number[]>([]);

  override formGroup = new FormGroup({
    linearScale: new FormControl<number | null>(null),
  });

  override ngOnInit() {
    super.ngOnInit();

    this.initialize();
  }

  override onQuestionInputChange(): void {
    const prevSelectedScale = this.formGroup.controls.linearScale.value;

    this.initialize();

    if (
      prevSelectedScale !== null &&
      (prevSelectedScale >= this.question().fromNumber ||
        prevSelectedScale <= this.question().toNumber)
    ) {
      this.formGroup.controls.linearScale.patchValue(prevSelectedScale);
    }
  }

  override getOnlyQuestionResponsePutReq(): OnlyLinearScaleResponsePutReq | null {
    const value = this.formGroup.value.linearScale;
    return value === null || value === undefined
      ? null
      : {
          scale: value,
        };
  }

  protected onRadioButtonCLick(value: number) {
    if (!this.question().required && this.formGroup.value.linearScale === value) {
      this.formGroup.controls.linearScale.patchValue(null);
      this.hasError.emit(this.formGroup.invalid);
    }
  }

  private initialize() {
    this.radioButtons.set(
      ArrayUtil.fillByNumbers(this.question().fromNumber, this.question().toNumber),
    );

    if (this.question().required) {
      this.formGroup.controls.linearScale.addValidators(Validators.required);
    } else {
      if (this.formGroup.controls.linearScale.hasValidator(Validators.required)) {
        this.formGroup.controls.linearScale.removeValidators(Validators.required);
      }
    }
    this.formGroup.controls.linearScale.updateValueAndValidity();
  }
}
