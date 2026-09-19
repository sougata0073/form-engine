import { Component, inject, OnChanges, OnInit, signal, SimpleChange } from '@angular/core';
import { EditFormQuestionComponent } from '../../../../type/edit-form-question-component';
import { MultipleChoiceRes } from '../../../../model/edit-form/question/response/multiple-choice-res';
import { MatButton } from '@angular/material/button';
import { EditFormMultipleChoiceOption } from './edit-form-multiple-choice-option/edit-form-multiple-choice-option';
import { EditFormStateService } from '../../../../service/edit-form-state-service';
import { MatDialog } from '@angular/material/dialog';
import { SimpleDialog } from '../../../../shared/simple-dialog/simple-dialog';
import { MultipleChoiceOption } from '../../../../type/multiple-choice-option';
import { OnlyMultipleChoiceAddUpdateReq } from '../../../../model/edit-form/question/addreq/multiple-choice-add-req';
import {
  MultipleChoiceUpdateReq,
  OnlyMultipleChoiceUpdateReq,
} from '../../../../model/edit-form/question/updatereq/multiple-choice-update-req';

@Component({
  selector: 'app-edit-form-multiple-choice',
  imports: [MatButton, EditFormMultipleChoiceOption],
  templateUrl: './edit-form-multiple-choice.html',
  styleUrl: './edit-form-multiple-choice.scss',
})
export class EditFormMultipleChoice
  extends EditFormQuestionComponent<MultipleChoiceRes, OnlyMultipleChoiceUpdateReq>
  implements OnInit
{
  protected options = signal<MultipleChoiceOption[]>([]);

  protected formStateService = inject(EditFormStateService);
  private dialog = inject(MatDialog);

  ngOnInit() {
    this.options.set(this.question().options.map((op) => ({ ...op, valid: !!op.option })));
  }

  override onQuestionInputChange(change: SimpleChange<MultipleChoiceRes>): void {
    this.options.set(this.question().options.map((op) => ({ ...op, valid: !!op.option })));
  }

  protected onAddOptionClick() {
    if (this.options().length >= 20) {
      this.dialog.open(SimpleDialog, {
        data: SimpleDialog.configure('Error', 'Can not add more than 20 options', 'Close'),
      });
      return;
    }

    const option = {
      id: 'NEW_' + crypto.randomUUID(),
      option: `Option ${this.options().length + 1}`,
      valid: true,
      orderIndex: this.options().length,
    };

    this.options.update((val) => {
      return [...val, option];
    });

    this.emitCanSaveHasError();

    this.updateQuestion.emit({
      req: {
        options: [
          {
            option: option.option,
            action: 'ADD',
          },
        ],
        updateFields: ['options' satisfies keyof MultipleChoiceUpdateReq],
      },
      updateType: 'CRITICAL',
    });
  }

  protected removeOption(optionId: string) {
    if (this.options().length <= 1) {
      this.dialog.open(SimpleDialog, {
        data: SimpleDialog.configure('Error', 'At least 1 option is required', 'Close'),
      });
      return;
    }

    this.options.update((val) => {
      return [...val.filter((v) => v.id !== optionId)];
    });

    this.emitCanSaveHasError();

    this.updateQuestion.emit({
      req: {
        options: [
          {
            id: optionId,
            action: 'DELETE',
          },
        ],
        updateFields: ['options' satisfies keyof MultipleChoiceUpdateReq],
      },
      updateType: 'CRITICAL',
    });
  }

  protected onOptionTextChange(option: MultipleChoiceOption) {
    this.options.update((val) =>
      val.map((v) => (v.id === option.id ? { ...v, option: option.option } : v)),
    );

    this.emitCanSaveHasError();

    this.updateQuestion.emit({
      options: [
        {
          id: option.id,
          option: option.option,
          action: 'UPDATE',
        },
      ],
      updateFields: ['options' satisfies keyof MultipleChoiceUpdateReq],
    });
  }

  protected onOptionCanSaveChange(optionId: string, canSave: boolean) {
    this.options.update((ops) => {
      return ops.map((op) => {
        return op.id === optionId ? { ...op, valid: canSave } : { ...op };
      });
    });
    this.emitCanSaveHasError();
  }

  protected emitCanSaveHasError() {
    const allValid = this.options().every((op) => op.valid);
    this.canSaveQuestion.emit(allValid && !!this.options().length);
    this.hasError.emit(!allValid);
  }
}
