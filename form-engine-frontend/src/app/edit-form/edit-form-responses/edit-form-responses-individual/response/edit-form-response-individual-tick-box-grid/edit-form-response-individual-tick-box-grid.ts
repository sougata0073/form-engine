import {Component, signal} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {
  TickBoxGridResponseIndividual
} from '../../../../../model/edit-form/responses/individual/tick-box-grid-response-individual';
import {
  OnlyTickBoxGridColumnRes,
  OnlyTickBoxGridRowRes,
  TickBoxGridRes
} from '../../../../../model/edit-form/question/response/tick-box-grid-res';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCheckbox} from '@angular/material/checkbox';

type Option = {
  row: OnlyTickBoxGridRowRes & { formArray: FormArray<FormControl<boolean | null>> },
  columns: (OnlyTickBoxGridColumnRes & { formControl: FormControl<boolean | null> })[]
}

@Component({
  selector: 'app-edit-form-response-individual-tick-box-grid',
  imports: [
    ReactiveFormsModule,
    MatCheckbox
  ],
  templateUrl: './edit-form-response-individual-tick-box-grid.html',
  styleUrl: './edit-form-response-individual-tick-box-grid.scss',
})
export class EditFormResponseIndividualTickBoxGrid extends EditFormResponseIndividualComponent<TickBoxGridRes, TickBoxGridResponseIndividual> {

  protected options = signal<Option[]>([])

  protected formGroup = new FormGroup<Record<string, FormArray<FormControl<boolean | null>>>>({})

  override ngOnInit() {
    super.ngOnInit();

    const options = this.question().rows.map(r => {
      const option: Option = {
        row: {
          ...r,
          formArray: new FormArray<FormControl<boolean | null>>([])
        },
        columns: []
      }

      this.question().columns.forEach(c => {
        const column = {
          ...c,
          formControl: new FormControl<boolean | null>(null)
        }

        option.row.formArray.push(column.formControl)
        option.columns.push(column)
      })

      this.formGroup.addControl(option.row.id, option.row.formArray)

      return option
    })

    const rowMap = new Map(this.response()?.rows.map(r => [r.rowId, r]))

    options.forEach(op => {
      op.columns.forEach(c => c.formControl.disable())

      const row = rowMap.get(op.row.id)

      if (row) {
        const columnIdSet = new Set(row.columnIds)

        op.columns.forEach(c => {
          c.formControl.setValue(columnIdSet.has(c.id))
        })
      }
    })

    this.options.set(options)

  }
}
