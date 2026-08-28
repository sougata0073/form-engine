import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogTitle} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-functional-dialog',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton
  ],
  templateUrl: './functional-dialog.html',
  styleUrl: './functional-dialog.scss',
})
export class FunctionalDialog {
  data = inject(MAT_DIALOG_DATA)

  static configure(
    title: string, description: string,
    btn1Text: string, onBtn1Click: () => void,
    btn2Text: string, onBtn2Click: () => void
  ) {
    return {
      title: title,
      description: description,
      btn1Text: btn1Text,
      onBtn1Click: onBtn1Click,
      btn2Text: btn2Text,
      onBtn2Click: onBtn2Click
    }
  }
}
