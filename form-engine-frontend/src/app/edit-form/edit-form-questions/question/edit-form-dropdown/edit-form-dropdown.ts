import {Component, inject, OnInit, signal} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {DropdownRes} from '../../../../model/edit-form/question/response/dropdown-res';
import {EditFormDropdownOption} from './edit-form-dropdown-option/edit-form-dropdown-option';
import {MatButton} from '@angular/material/button';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {MatDialog} from '@angular/material/dialog';
import {DropdownOption} from '../../../../type/dropdown-option';
import {SimpleDialog} from '../../../../shared/simple-dialog/simple-dialog';
import {OnlyDropdownAddUpdateReq} from '../../../../model/edit-form/question/request/dropdown-add-update-req';

@Component({
  selector: 'app-edit-form-dropdown',
  imports: [
    EditFormDropdownOption,
    MatButton
  ],
  templateUrl: './edit-form-dropdown.html',
  styleUrl: './edit-form-dropdown.scss',
})
export class EditFormDropdown extends EditFormQuestionComponent<DropdownRes, OnlyDropdownAddUpdateReq> implements OnInit {

  protected options = signal<DropdownOption[]>([])

  protected formStateService = inject(EditFormStateService)
  private dialog = inject(MatDialog)

  ngOnInit() {
    this.options.update(() => {
      return this.question().options
        .map((op, index) => ({...op, valid: !!op.option}))
    })
  }

  override getOnlyQuestionAddUpdateReq(): OnlyDropdownAddUpdateReq {
    return {
      options: this.options().map(val => {
        return {
          id: val.id.startsWith('NEW_') ? null : val.id,
          option: val.option
        }
      })
    }
  }

  protected onAddOptionClick() {
    if (this.options().length >= 20) {
      this.dialog.open(
        SimpleDialog, {
          data: SimpleDialog.configure('Error', 'Can not add more than 20 options', 'Close')
        }
      )
      return
    }
    this.options.update(val => [...val, {
      id: 'NEW_' + crypto.randomUUID(),
      orderIndex: val.length,
      option: `Option ${val.length + 1}`,
      valid: true
    }])
    this.emitCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected removeOption(optionId: string) {
    this.options.update(val => {
        const newArray = val
          .filter(v => v.id !== optionId)
          .map((v, index) => {
            return {...v, orderIndex: index}
          })

        return [...newArray]
      }
    )
    this.emitCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onOptionTextChange(option: DropdownOption) {
    this.options.update(val =>
      val.map(v => v.id === option.id ? {...v, option: option.option} : v))
    this.emitCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onOptionCanSaveChange(optionId: string, canSave: boolean) {
    this.options.update(ops => {
      return ops.map(op => {
        return op.id === optionId ? {...op, valid: canSave} : {...op}
      })
    })
    this.emitCanSaveHasError()
  }

  protected emitCanSaveHasError() {
    const allValid = this.options().every(op => op.valid)
    this.canSaveQuestion.emit(allValid)
    this.hasError.emit(!allValid)
  }

}
