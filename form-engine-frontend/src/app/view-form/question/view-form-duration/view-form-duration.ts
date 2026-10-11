import { Component } from '@angular/core';
import { ViewFormQuestionComponent } from '../../../type/view-form-question-component';
import { DurationRes } from '../../../model/edit-form/question/response/duration-res';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatError, MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { OnlyDurationResponsePutReq } from '../../../model/view-form/request/duration-response-put-req';

@Component({
  selector: 'app-view-form-duration',
  imports: [MatFormField, MatInput, MatLabel, ReactiveFormsModule, MatError],
  templateUrl: './view-form-duration.html',
  styleUrl: './view-form-duration.scss',
})
export class ViewFormDuration extends ViewFormQuestionComponent<
  DurationRes,
  OnlyDurationResponsePutReq
> {
  override formGroup = new FormGroup({
    hours: new FormControl<number | null>(null, [Validators.min(0), Validators.max(72)]),
    minutes: new FormControl<number | null>(null, [Validators.min(0), Validators.max(59)]),
    seconds: new FormControl<number | null>(null, [Validators.min(0), Validators.max(59)]),
  });

  override ngOnInit(): void {
    super.ngOnInit();

    this.initialize();

    this.formGroup.controls.hours.valueChanges.subscribe((val) => {
      if (this.formGroup.value.minutes === null || this.formGroup.value.minutes === undefined) {
        this.formGroup.controls.minutes.patchValue(0, { emitEvent: false });
      }
      if (this.formGroup.value.seconds === null || this.formGroup.value.seconds === undefined) {
        this.formGroup.controls.seconds.patchValue(0, { emitEvent: false });
      }
    });

    this.formGroup.controls.minutes.valueChanges.subscribe((val) => {
      if (this.formGroup.value.hours === null || this.formGroup.value.hours === undefined) {
        this.formGroup.controls.hours.patchValue(0, { emitEvent: false });
      }
      if (this.formGroup.value.seconds === null || this.formGroup.value.seconds === undefined) {
        this.formGroup.controls.seconds.patchValue(0, { emitEvent: false });
      }
    });

    this.formGroup.controls.seconds.valueChanges.subscribe((val) => {
      if (this.formGroup.value.hours === null || this.formGroup.value.hours === undefined) {
        this.formGroup.controls.hours.patchValue(0, { emitEvent: false });
      }
      if (this.formGroup.value.minutes === null || this.formGroup.value.minutes === undefined) {
        this.formGroup.controls.minutes.patchValue(0, { emitEvent: false });
      }
    });
  }

  override onQuestionInputChange(): void {
    this.initialize();
  }

  override getOnlyQuestionResponsePutReq(): OnlyDurationResponsePutReq | null {
    const values = this.formGroup.value;
    const h = values.hours;
    const m = values.minutes;
    const s = values.seconds;

    if (h === null && m === null && s === null) return null;

    return {
      hours: h ?? 0,
      minutes: m ?? 0,
      seconds: s ?? 0,
    };
  }

  private initialize() {
    if (this.question().required) {
      Object.values(this.formGroup.controls).forEach((control) => {
        control.addValidators(Validators.required);
      });
    } else {
      Object.values(this.formGroup.controls).forEach((control) => {
        if (control.hasValidator(Validators.required)) {
          control.removeValidators(Validators.required);
        }
      });
    }
    
    Object.values(this.formGroup.controls).forEach(control => {
      control.updateValueAndValidity()
    })
  }
}
