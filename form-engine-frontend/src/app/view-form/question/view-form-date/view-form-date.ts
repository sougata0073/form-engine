import { Component, OnInit, SimpleChanges } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatFormField, MatHint, MatInput, MatLabel } from '@angular/material/input';
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerToggle,
} from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ViewFormQuestionComponent } from '../../../type/view-form-question-component';
import { DateRes } from '../../../model/edit-form/question/response/date-res';
import { OnlyDateResponsePutReq } from '../../../model/view-form/request/date-response-put-req';

@Component({
  selector: 'app-view-form-date',
  imports: [
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatDatepickerInput,
    MatHint,
    MatDatepickerToggle,
    MatDatepicker,
    FormsModule,
    MatFormFieldModule,
  ],
  providers: [provideNativeDateAdapter()],
  templateUrl: './view-form-date.html',
  styleUrl: './view-form-date.scss',
})
export class ViewFormDate
  extends ViewFormQuestionComponent<DateRes, OnlyDateResponsePutReq>
  implements OnInit
{
  override formGroup = new FormGroup({
    date: new FormControl<Date | null>(null),
  });

  override ngOnInit(): void {
    super.ngOnInit();

    this.initialize();
  }

  override getOnlyQuestionResponsePutReq(): OnlyDateResponsePutReq | null {
    const value = this.formGroup.value.date;
    return value ? { date: value } : null;
  }

  override onQuestionInputChange(): void {
    this.initialize();
  }

  private initialize() {
    if (this.question().required) {
      this.formGroup.controls.date.addValidators(Validators.required);
    } else {
      if (this.formGroup.controls.date.hasValidator(Validators.required)) {
        this.formGroup.controls.date.removeValidators(Validators.required);
      }
    }
    this.formGroup.controls.date.updateValueAndValidity();
  }
}
