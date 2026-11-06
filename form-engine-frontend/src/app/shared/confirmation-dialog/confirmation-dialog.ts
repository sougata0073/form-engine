import { Component, inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';

@Component({
  selector: 'app-mandatory-dialog',
  imports: [MatDialogTitle, MatDialogContent, MatDialogActions, MatButton],
  templateUrl: './confirmation-dialog.html',
  styleUrl: './confirmation-dialog.scss',
})
export class ConfirmationDialog {
  data = inject(MAT_DIALOG_DATA);

  static configure(title: string, description: string, btnText: string, onBtnClick: () => void) {
    return {
      title: title,
      description: description,
      btnText: btnText,
      onBtnClick: onBtnClick,
    };
  }
}
