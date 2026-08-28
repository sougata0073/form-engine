import {Component, inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-confirm-dialog',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton,
    MatDialogClose
  ],
  templateUrl: './simple-dialog.html',
  styleUrl: './simple-dialog.scss',
})
export class SimpleDialog {

  data = inject(MAT_DIALOG_DATA)

  static configure(title: string, description: string, positiveButtonText: string, negativeButtonText?: string) {
    return {
      title: title,
      description: description,
      positiveButtonText: positiveButtonText,
      negativeButtonText: negativeButtonText
    }
  }
}
