import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {MultipleChoiceGridRes} from '../../../model/edit-form/question/response/multiple-choice-grid-res';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {ValueLabelPair} from '../../../type/value-label-pair';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {MatButton} from '@angular/material/button';
import {MatError} from '@angular/material/input';
import {
  OnlyMultipleChoiceGridResponsePutReq,
  OnlyMultipleChoiceGridRowResponsePutReq
} from '../../../model/view-form/request/multiple-choice-grid-response-put-req';

type Option = {
  row: ValueLabelPair & { control: FormControl<string | null> },
  columns: ValueLabelPair[]
}

@Component({
  selector: 'app-view-form-multiple-choice-grid',
  imports: [
    MatRadioGroup,
    MatRadioButton,
    ReactiveFormsModule,
    MatButton,
    MatError
  ],
  templateUrl: './view-form-multiple-choice-grid.html',
  styleUrl: './view-form-multiple-choice-grid.scss',
})
export class ViewFormMultipleChoiceGrid extends ViewFormQuestionComponent<MultipleChoiceGridRes, OnlyMultipleChoiceGridResponsePutReq> {

  protected options = signal<Option[]>([])

  protected showClearSelection = signal<boolean>(false)

  override formGroup =
    new FormGroup<Record<string, FormControl<string | null>>>({})

  override ngOnInit() {
    this.options.set(
      this.question().rows.map(r => {
        const option: Option = {
          row: {
            value: r.id,
            label: r.row,
            control: new FormControl<string | null>(null)
          },
          columns: []
        }
        this.question().columns.forEach(c => {
          const column = {
            value: c.id,
            label: c.column
          }
          option.columns.push(column)
        })

        this.formGroup.addControl(option.row.value, option.row.control)
        return option
      })
    )

    this.formGroup.valueChanges.subscribe(() => {
      const show = Object.values(this.formGroup.value).some(val => val)
      this.showClearSelection.set(show)
    })

    super.ngOnInit();
  }

  override getOnlyQuestionResponsePutReq(): OnlyMultipleChoiceGridResponsePutReq | null {

    const rows: OnlyMultipleChoiceGridRowResponsePutReq[] = this.options()
      .filter(op => op.row.control.value !== null)
      .map(op => {
        return {
          rowId: op.row.value,
          responseColumnId: op.row.control.value!
        }
      })

    return rows.length === 0 || rows.every(r => r.responseColumnId === null) ?
      null : {rows: rows}
  }

  protected onRadioButtonCLick(controlName: string, value: string) {
    if (this.question().required) return

    const control = this.formGroup.get(controlName)
    if (control?.value === value) {
      control.patchValue(null)
      this.hasError.emit(this.formGroup.invalid)
    }
  }

  protected onClearSelectionClick() {
    Object.values(this.formGroup.controls).forEach(control => {
      control.patchValue(null)
    })
  }
}
