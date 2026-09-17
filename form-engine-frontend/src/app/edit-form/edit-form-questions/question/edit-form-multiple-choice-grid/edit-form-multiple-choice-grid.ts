import { Component, inject, OnInit, signal } from '@angular/core';
import { EditFormQuestionComponent } from '../../../../type/edit-form-question-component';
import { MultipleChoiceGridRes } from '../../../../model/edit-form/question/response/multiple-choice-grid-res';
import { EditFormStateService } from '../../../../service/edit-form-state-service';
import { EditFormDropdownOption } from '../edit-form-dropdown/edit-form-dropdown-option/edit-form-dropdown-option';
import { DropdownOption } from '../../../../type/dropdown-option';
import { MatButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { SimpleDialog } from '../../../../shared/simple-dialog/simple-dialog';
import {
  EditFormMultipleChoiceOption
} from '../edit-form-multiple-choice/edit-form-multiple-choice-option/edit-form-multiple-choice-option';
import { MultipleChoiceOption } from '../../../../type/multiple-choice-option';
import { MatRadioButton, MatRadioGroup } from '@angular/material/radio';
import {
  OnlyMultipleChoiceGridAddUpdateReq
} from '../../../../model/edit-form/question/addreq/multiple-choice-grid-add-req';
import { MultipleChoiceGridUpdateReq, OnlyMultipleChoiceGridUpdateReq } from '../../../../model/edit-form/question/updatereq/multiple-choice-grid-update-req';

@Component({
  selector: 'app-edit-form-multiple-choice-grid',
  imports: [
    EditFormDropdownOption,
    MatButton,
    EditFormMultipleChoiceOption,
    MatRadioButton,
    MatRadioGroup,
  ],
  templateUrl: './edit-form-multiple-choice-grid.html',
  styleUrl: './edit-form-multiple-choice-grid.scss',
})
export class EditFormMultipleChoiceGrid extends EditFormQuestionComponent<MultipleChoiceGridRes, OnlyMultipleChoiceGridUpdateReq> implements OnInit {

  protected rows = signal<DropdownOption[]>([])
  protected columns = signal<MultipleChoiceOption[]>([])

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
        updateFields: ['row' satisfies keyof MultipleChoiceGridUpdateReq]
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
        updateFields: ['row' satisfies keyof MultipleChoiceGridUpdateReq]
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
        updateFields: ['row' satisfies keyof MultipleChoiceGridUpdateReq]
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
        updateFields: ['column' satisfies keyof MultipleChoiceGridUpdateReq]
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
        updateFields: ['column' satisfies keyof MultipleChoiceGridUpdateReq]
      }
    )
  }

  protected onColumnTextChange(column: MultipleChoiceOption) {
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
        updateFields: ['column' satisfies keyof MultipleChoiceGridUpdateReq]
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
