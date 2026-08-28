import {Component, inject, OnInit, signal} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {MultipleChoiceRes} from '../../../../model/edit-form/question/response/multiple-choice-res';
import {MatButton} from '@angular/material/button';
import {EditFormMultipleChoiceOption} from './edit-form-multiple-choice-option/edit-form-multiple-choice-option';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {MatDialog} from '@angular/material/dialog';
import {SimpleDialog} from '../../../../shared/simple-dialog/simple-dialog';
import {MultipleChoiceOption} from '../../../../type/multiple-choice-option';
import {
  OnlyMultipleChoiceAddUpdateReq
} from '../../../../model/edit-form/question/request/multiple-choice-add-update-req';

@Component({
  selector: 'app-edit-form-multiple-choice',
  imports: [
    MatButton,
    EditFormMultipleChoiceOption
  ],
  templateUrl: './edit-form-multiple-choice.html',
  styleUrl: './edit-form-multiple-choice.scss',
})
export class EditFormMultipleChoice extends EditFormQuestionComponent<MultipleChoiceRes, OnlyMultipleChoiceAddUpdateReq> implements OnInit {

  protected options = signal<MultipleChoiceOption[]>([])

  protected formStateService = inject(EditFormStateService)
  private dialog = inject(MatDialog)

  ngOnInit() {
    this.options.set(this.question().options
      .map((op) => ({...op, valid: !!op.option}))
    )
  }

  override getOnlyQuestionAddUpdateReq(): OnlyMultipleChoiceAddUpdateReq {
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
    this.options.update(val => {
      const option = {
        id: 'NEW_' + crypto.randomUUID(),
        option: `Option ${val.length + 1}`,
        valid: true,
        orderIndex: val.length
      }

      return [...val, option]
    })

    this.emitCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected removeOption(optionId: string) {
    this.options.update(val => {
      return [...val.filter(v => v.id !== optionId)]
    })

    this.emitCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected onOptionTextChange(option: MultipleChoiceOption) {
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
