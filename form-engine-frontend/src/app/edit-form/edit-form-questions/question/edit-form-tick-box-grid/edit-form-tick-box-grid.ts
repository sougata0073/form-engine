import {Component, inject, OnInit, signal} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {TickBoxGridRes} from '../../../../model/edit-form/question/response/tick-box-grid-res';
import {DropdownOption} from '../../../../type/dropdown-option';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {MatDialog} from '@angular/material/dialog';
import {SimpleDialog} from '../../../../shared/simple-dialog/simple-dialog';
import {CheckboxOption} from '../../../../type/checkbox-option';
import {EditFormDropdownOption} from '../edit-form-dropdown/edit-form-dropdown-option/edit-form-dropdown-option';
import {EditFormCheckboxOption} from '../edit-form-checkbox/edit-form-checkbox-option/edit-form-checkbox-option';
import {MatButton} from '@angular/material/button';
import {MatCheckbox} from '@angular/material/checkbox';
import {ReactiveFormsModule} from '@angular/forms';
import {OnlyTickBoxGridAddUpdateReq} from '../../../../model/edit-form/question/request/tick-box-grid-add-update-req';

@Component({
  selector: 'app-edit-form-tick-box-grid',
  imports: [
    EditFormDropdownOption,
    EditFormCheckboxOption,
    MatButton,
    MatCheckbox,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-tick-box-grid.html',
  styleUrl: './edit-form-tick-box-grid.scss',
})
export class EditFormTickBoxGrid extends EditFormQuestionComponent<TickBoxGridRes, OnlyTickBoxGridAddUpdateReq> implements OnInit {

  protected rows = signal<DropdownOption[]>([])
  protected columns = signal<CheckboxOption[]>([])

  protected formStateService = inject(EditFormStateService)
  private dialog = inject(MatDialog)

  ngOnInit() {

    this.rows.set(this.question().rows
      .map(r => ({id: r.id, option: r.row, orderIndex: r.orderIndex, valid: !!r.row}))
    )

    this.columns.set(this.question().columns
      .map(r => ({id: r.id, option: r.column, orderIndex: r.orderIndex, valid: !!r.column}))
    )
  }

  override getOnlyQuestionAddUpdateReq(): OnlyTickBoxGridAddUpdateReq {
    return {
      eachRowRequired: false,
      rows: this.rows().map(r => {
        return {
          id: r.id.startsWith('NEW_') ? null : r.id,
          row: r.option
        }
      }),
      columns: this.columns().map(c => {
        return {
          id: c.id.startsWith('NEW_') ? null : c.id,
          column: c.option
        }
      })
    }
  }

  protected addRow() {
    if (this.rows().length >= 20) {
      this.dialog.open(
        SimpleDialog, {
          data: SimpleDialog.configure('Error', 'Can not add more than 20 row', 'Close')
        }
      )
      return
    }
    this.rows.update(val => [...val, {
      id: 'NEW_' + crypto.randomUUID(),
      orderIndex: val.length,
      option: `Row ${val.length + 1}`,
      valid: true
    }])
    this.emiCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected removeRow(rowId: string) {
    this.rows.update(val => {
        const newArray = val
          .filter(v => v.id !== rowId)
          .map((v, index) => {
            return {...v, orderNumber: index + 1}
          })

        return [...newArray]
      }
    )

    this.emiCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onRowTextChange(row: DropdownOption) {
    this.rows.update(val =>
      val.map(v => v.id === row.id ? {...v, option: row.option} : v))
    this.emiCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onRowCanSaveChange(rowId: string, canSave: boolean) {
    this.rows.update(ops => {
      return ops.map(op => {
        return op.id === rowId ? {...op, valid: canSave} : {...op}
      })
    })
    this.emiCanSaveHasError()
  }

  protected addColumn() {
    if (this.columns().length >= 20) {
      this.dialog.open(
        SimpleDialog, {
          data: SimpleDialog.configure(
            'Error',
            'Can not add more than 20 columns',
            'Close'
          )
        }
      )
      return
    }
    this.columns.update(val => {
      const option = {
        id: 'NEW_' + crypto.randomUUID(),
        orderIndex: val.length,
        option: `Column ${val.length + 1}`,
        valid: true
      }

      return [...val, option]
    })
    this.emiCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected removeColumn(columnId: string) {
    this.columns.update(val => {
      return [...val.filter(v => v.id !== columnId)]
    })
    this.emiCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onColumnTextChange(column: CheckboxOption) {
    this.columns.update(val => {
      return val.map(v => v.id === column.id ? {...v, option: column.option} : v)
    })
    this.emiCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onColumnCanSaveChange(columnId: string, canSave: boolean) {
    this.columns.update(ops => {
      return ops.map(op => {
        return op.id === columnId ? {...op, valid: canSave} : {...op}
      })
    })
    this.emiCanSaveHasError()
  }

  protected emiCanSaveHasError() {
    const allRowsValid = this.rows().every(r => r.valid)
    const allColumnsValid = this.columns().every(c => c.valid)

    this.canSaveQuestion.emit(
      allRowsValid && allColumnsValid && !!this.rows().length && !!this.columns().length
    )
    this.hasError.emit(!allRowsValid || !allColumnsValid)
  }

}
