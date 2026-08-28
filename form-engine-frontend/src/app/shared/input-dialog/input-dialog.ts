import {Component, inject, OnInit} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatFormField, MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {FormControl, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-input-dialog',
  imports: [
    MatDialogContent,
    MatFormField,
    MatDialogActions,
    MatButton,
    MatDialogClose,
    MatDialogTitle,
    MatInput,
    ReactiveFormsModule
  ],
  templateUrl: './input-dialog.html',
  styleUrl: './input-dialog.scss',
})
export class InputDialog implements OnInit {
  readonly data = inject(MAT_DIALOG_DATA);

  formControl = new FormControl<string>('');

  static configure(
    title: string, description: string | null, inputDefaultValue: string,
    btn1Text: string, onBtn1Click: () => void,
    btn2Text: string, onBtn2Click: () => void
  ) {
    return {
      title: title,
      description: description,
      inputDefaultValue: inputDefaultValue,
      btn1Text: btn1Text,
      onBtn1Click: onBtn1Click,
      btn2Text: btn2Text,
      onBtn2Click: onBtn2Click
    }
  }

  ngOnInit() {
    this.formControl.setValue(this.data.inputDefaultValue);
  }
}
