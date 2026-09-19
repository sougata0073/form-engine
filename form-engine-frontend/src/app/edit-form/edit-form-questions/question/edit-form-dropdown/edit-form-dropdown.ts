import { Component, inject, OnInit, signal, SimpleChange } from '@angular/core';
import { EditFormQuestionComponent } from '../../../../type/edit-form-question-component';
import { DropdownRes } from '../../../../model/edit-form/question/response/dropdown-res';
import { EditFormDropdownOption } from './edit-form-dropdown-option/edit-form-dropdown-option';
import { MatButton } from '@angular/material/button';
import { EditFormStateService } from '../../../../service/edit-form-state-service';
import { MatDialog } from '@angular/material/dialog';
import { DropdownOption } from '../../../../type/dropdown-option';
import { SimpleDialog } from '../../../../shared/simple-dialog/simple-dialog';
import { OnlyDropdownAddUpdateReq } from '../../../../model/edit-form/question/addreq/dropdown-add-req';
import {
  DropdownUpdateReq,
  OnlyDropdownUpdateReq,
} from '../../../../model/edit-form/question/updatereq/dropdown-update-req';

@Component({
  selector: 'app-edit-form-dropdown',
  imports: [EditFormDropdownOption, MatButton],
  templateUrl: './edit-form-dropdown.html',
  styleUrl: './edit-form-dropdown.scss',
})
export class EditFormDropdown
  extends EditFormQuestionComponent<DropdownRes, OnlyDropdownUpdateReq>
  implements OnInit
{
  protected options = signal<DropdownOption[]>([]);

  protected formStateService = inject(EditFormStateService);
  private dialog = inject(MatDialog);

  ngOnInit() {
    this.options.set(this.question().options.map((op, index) => ({ ...op, valid: !!op.option })));
  }

  override onQuestionInputChange(change: SimpleChange<DropdownRes>): void {
    this.options.set(this.question().options.map((op, index) => ({ ...op, valid: !!op.option })));
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
      orderIndex: this.options().length,
      option: `Option ${this.options().length + 1}`,
      valid: true,
    };

    this.options.update((val) => [...val, option]);

    this.emitCanSaveHasError();

    this.updateQuestion.emit({
      req: {
        options: [
          {
            option: option.option,
            action: 'ADD',
          },
        ],
        updateFields: ['options' satisfies keyof DropdownUpdateReq],
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
      const newArray = val
        .filter((v) => v.id !== optionId)
        .map((v, index) => {
          return { ...v, orderIndex: index };
        });

      return [...newArray];
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
        updateFields: ['options' satisfies keyof DropdownUpdateReq],
      },
      updateType: 'CRITICAL',
    });
  }

  protected onOptionTextChange(option: DropdownOption) {
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
      updateFields: ['options' satisfies keyof DropdownUpdateReq],
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
