import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {TickBoxGridRes} from '../../../model/edit-form/question/response/tick-box-grid-res';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatButton} from "@angular/material/button";
import {MatCheckbox} from '@angular/material/checkbox';
import {ValueLabelPair} from '../../../type/value-label-pair';
import {FormArrayValidator} from '../../../formValidator/form-array-validator';
import {MatError} from '@angular/material/input';
import {
  OnlyTickBoxGridResponsePutReq,
  OnlyTickBoxGridRowResponsePutReq
} from '../../../model/view-form/request/tick-box-grid-response-put-req';

type Option = {
  row: ValueLabelPair & { formArray: FormArray<FormControl<boolean | null>> },
  columns: (ValueLabelPair & { control: FormControl<boolean | null> })[]
}

@Component({
  selector: 'app-view-form-tick-box-grid',
  imports: [
    MatButton,
    ReactiveFormsModule,
    MatCheckbox,
    MatError
  ],
  templateUrl: './view-form-tick-box-grid.html',
  styleUrl: './view-form-tick-box-grid.scss',
})
export class ViewFormTickBoxGrid extends ViewFormQuestionComponent<TickBoxGridRes, OnlyTickBoxGridResponsePutReq> {

  protected options = signal<Option[]>([])

  protected showClearSelection = signal<boolean>(false)

  override formGroup =
    new FormGroup<Record<string, FormArray<FormControl<boolean | null>>>>({})

  override ngOnInit() {
    super.ngOnInit();

    this.options.set(
      this.question().rows.map(r => {
        const option: Option = {
          row: {
            value: r.id,
            label: r.row,
            formArray: new FormArray<FormControl<boolean | null>>([])
          },
          columns: []
        }
        this.question().columns.forEach(c => {
          const column = {
            value: c.id,
            label: c.column,
            control: new FormControl<boolean | null>(null)
          }
          option.row.formArray.push(column.control)
          option.columns.push(column)
        })

        if (this.question().required) {
          option.row.formArray.addValidators(FormArrayValidator.requiredAtLeast(1))
        }

        this.formGroup.addControl(option.row.value, option.row.formArray)
        return option
      })
    )

    this.formGroup.valueChanges.subscribe(values => {
      const show = Object.values(values)
        .some(array => array?.some(val => val))

      this.showClearSelection.set(show)
    })
  }

  override getOnlyQuestionResponsePutReq(): OnlyTickBoxGridResponsePutReq | null {
    const rows: OnlyTickBoxGridRowResponsePutReq[] = this.options()
      .filter(op => op.columns.some(c => c.control.value))
      .map(op => {
        return {
          rowId: op.row.value,
          responseColumnIds: op.columns
            .filter(c => c.control.value)
            .map(c => c.value)
        }
      })

    return rows.length === 0 || rows.every(r => r.responseColumnIds.every(id => id === null)) ?
      null : {rows: rows}
  }

  override clearForm() {
    Object.values(this.formGroup.controls).forEach(arr => {
      arr.controls.forEach(ctr => ctr.patchValue(null))
    })
  }

  protected onClearSelectionClick() {
    Object.values(this.formGroup.controls).forEach(formArray => {
      Object.values(formArray.controls).forEach(control => {
        control.patchValue(null)
      })
    })
  }
}
