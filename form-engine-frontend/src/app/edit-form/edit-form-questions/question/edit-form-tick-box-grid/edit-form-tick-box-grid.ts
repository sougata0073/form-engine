import { Component, inject, OnInit, signal } from '@angular/core';
import { EditFormQuestionComponent } from '../../../../type/edit-form-question-component';
import { TickBoxGridRes } from '../../../../model/edit-form/question/response/tick-box-grid-res';
import { DropdownOption } from '../../../../type/dropdown-option';
import { EditFormStateService } from '../../../../service/edit-form-state-service';
import { MatDialog } from '@angular/material/dialog';
import { SimpleDialog } from '../../../../shared/simple-dialog/simple-dialog';
import { CheckboxOption } from '../../../../type/checkbox-option';
import { EditFormDropdownOption } from '../edit-form-dropdown/edit-form-dropdown-option/edit-form-dropdown-option';
import { EditFormCheckboxOption } from '../edit-form-checkbox/edit-form-checkbox-option/edit-form-checkbox-option';
import { MatButton } from '@angular/material/button';
import { MatCheckbox } from '@angular/material/checkbox';
import { ReactiveFormsModule } from '@angular/forms';
import { OnlyTickBoxGridAddUpdateReq } from '../../../../model/edit-form/question/addreq/tick-box-grid-add-req';
import { OnlyTickBoxGridUpdateReq, TickBoxGridUpdateReq } from '../../../../model/edit-form/question/updatereq/tick-box-grid-update-req';

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
export class EditFormTickBoxGrid extends EditFormQuestionComponent<TickBoxGridRes, OnlyTickBoxGridUpdateReq> implements OnInit {

  protected rows = signal<DropdownOption[]>([])
  protected columns = signal<CheckboxOption[]>([])

  protected formStateService = inject(EditFormStateService)
  private dialog = inject(MatDialog)

  ngOnInit() {

    this.rows.set(this.question().rows
      .map(r => ({ id: r.id, option: r.row, orderIndex: r.orderIndex, valid: !!r.row }))
    )

    this.columns.set(this.question().columns
      .map(r => ({ id: r.id, option: r.column, orderIndex: r.orderIndex, valid: !!r.column }))
    )
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

    const row = {
      id: 'NEW_' + crypto.randomUUID(),
      orderIndex: this.rows().length,
      option: `Row ${this.rows().length + 1}`,
      valid: true
    }

    this.rows.update(val => [...val, row])

    this.emiCanSaveHasError()

    this.updateQuestion.emit(
      {
        row: {
          row: row.option,
          action: 'ADD'
        },
        updateFields: ['row' satisfies keyof TickBoxGridUpdateReq]
      }
    )
  }

  protected removeRow(rowId: string) {
    if (this.rows().length <= 1) {
      this.dialog.open(
        SimpleDialog, {
        data: SimpleDialog.configure('Error', 'At least 1 row is required', 'Close')
      }
      )
      return
    }
    this.rows.update(val => {
      const newArray = val
        .filter(v => v.id !== rowId)
        .map((v, index) => {
          return { ...v, orderNumber: index + 1 }
        })

      return [...newArray]
    }
    )

    this.emiCanSaveHasError()

    this.updateQuestion.emit(
      {
        row: {
          id: rowId,
          action: 'DELETE'
        },
        updateFields: ['row' satisfies keyof TickBoxGridUpdateReq]
      }
    )
  }

  protected onRowTextChange(row: DropdownOption) {
    this.rows.update(val =>
      val.map(v => v.id === row.id ? { ...v, option: row.option } : v))
    this.emiCanSaveHasError()

    this.updateQuestion.emit(
      {
        row: {
          id: row.id,
          row: row.option,
          action: 'UPDATE'
        },
        updateFields: ['row' satisfies keyof TickBoxGridUpdateReq]
      }
    )
  }

  protected onRowCanSaveChange(rowId: string, canSave: boolean) {
    this.rows.update(ops => {
      return ops.map(op => {
        return op.id === rowId ? { ...op, valid: canSave } : { ...op }
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

    const column = {
      id: 'NEW_' + crypto.randomUUID(),
      orderIndex: this.columns().length,
      option: `Column ${this.columns().length + 1}`,
      valid: true
    }

    this.columns.update(val => {
      return [...val, column]
    })
    this.emiCanSaveHasError()

    this.updateQuestion.emit(
      {
        column: {
          column: column.option,
          action: 'ADD'
        },
        updateFields: ['column' satisfies keyof TickBoxGridUpdateReq]
      }
    )
  }

  protected removeColumn(columnId: string) {
    if (this.columns().length <= 1) {
      this.dialog.open(
        SimpleDialog, {
        data: SimpleDialog.configure(
          'Error',
          'At least 1 column is required',
          'Close'
        )
      }
      )
      return
    }
    this.columns.update(val => {
      return [...val.filter(v => v.id !== columnId)]
    })
    this.emiCanSaveHasError()

    this.updateQuestion.emit(
      {
        column: {
          id: columnId,
          action: 'DELETE'
        },
        updateFields: ['column' satisfies keyof TickBoxGridUpdateReq]
      }
    )
  }

  protected onColumnTextChange(column: CheckboxOption) {
    this.columns.update(val => {
      return val.map(v => v.id === column.id ? { ...v, option: column.option } : v)
    })
    this.emiCanSaveHasError()

    this.updateQuestion.emit(
      {
        column: {
          id: column.id,
          column: column.option,
          action: 'UPDATE'
        },
        updateFields: ['column' satisfies keyof TickBoxGridUpdateReq]
      }
    )
  }

  protected onColumnCanSaveChange(columnId: string, canSave: boolean) {
    this.columns.update(ops => {
      return ops.map(op => {
        return op.id === columnId ? { ...op, valid: canSave } : { ...op }
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
