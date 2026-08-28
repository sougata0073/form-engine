import {Component, effect, inject, model, OnDestroy, OnInit, signal} from '@angular/core';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatDivider} from '@angular/material/list';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {EditFormService} from '../../service/edit-form-service';
import {DatePipe} from '@angular/common';
import {MatButton, MatIconButton} from '@angular/material/button';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {MatTooltip} from '@angular/material/tooltip';
import {MatIcon} from '@angular/material/icon';
import {MatMenu, MatMenuTrigger} from '@angular/material/menu';
import {MatCalendar} from '@angular/material/datepicker';
import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from '@angular/material/timepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatCard} from '@angular/material/card';
import {ObjectUtil} from '../../util/object-util';

@Component({
  selector: 'app-stop-accepting-response-dialog',
  imports: [
    MatDialogTitle,
    MatDivider,
    MatDialogContent,
    MatRadioGroup,
    MatRadioButton,
    DatePipe,
    MatButton,
    ReactiveFormsModule,
    MatDialogActions,
    MatFormField,
    MatInput,
    MatDialogClose,
    MatIconButton,
    MatTooltip,
    MatIcon,
    MatMenu,
    MatMenuTrigger,
    MatLabel,
    MatSuffix,
    MatTimepicker,
    MatTimepickerInput,
    MatTimepickerToggle,
    MatCard,
    MatCalendar,
    MatError
  ],
  templateUrl: './stop-accepting-response-dialog.html',
  providers: [provideNativeDateAdapter()],
  styleUrl: './stop-accepting-response-dialog.scss',
})
export class StopAcceptingResponseDialog implements OnInit, OnDestroy {

  protected editFormService = inject(EditFormService)
  protected dialogRef = inject(MatDialogRef<StopAcceptingResponseDialog>)

  protected formInfo = this.editFormService.formInfo

  protected selectedDate = model<Date | null>(null)

  protected selectedDateTime = signal<Date | null>(null)
  protected chooseDateTimeClicked = signal<boolean>(false)
  protected editClicked = signal<boolean>(false)
  protected dateTimeOkClickedOnce = signal<boolean>(false)

  protected showDateError = signal<boolean>(false)
  protected canSave = signal<boolean>(false)

  protected formGroup = new FormGroup({
    radioGroup: new FormControl<'date' | 'response-limit' | null>(null),
    notAcceptingResponseMessage: new FormControl<string | null>(null),
    date: new FormControl<Date | null>(null, [Validators.required]),
    time: new FormControl<Date | null>(null, [Validators.required]),
    responseLimit: new FormControl<number | null>(null, [Validators.required, Validators.min(1), Validators.max(1000)])
  })

  protected selectedDateEffect = effect(() => {
    this.formGroup.controls.date.patchValue(this.selectedDate())
    this.showDateError.set(this.dateTimeOkClickedOnce() && !this.selectedDate())
    this.setCanSave()
  })

  ngOnInit() {
    let radioGroupValue: 'date' | 'response-limit' | null = null;

    if (this.formInfo()!.stopAcceptingResponseOn === null &&
      this.formInfo()!.stopAcceptingResponseAfterResponse === null) {
      radioGroupValue = 'date'
    } else if (this.formInfo()!.stopAcceptingResponseOn) {
      radioGroupValue = 'date'
    } else if (this.formInfo()!.stopAcceptingResponseAfterResponse >= 0) {
      radioGroupValue = 'response-limit'
    }

    this.formGroup.patchValue({
      radioGroup: radioGroupValue,
      responseLimit: this.formInfo()!.stopAcceptingResponseAfterResponse,
      notAcceptingResponseMessage: this.formInfo()!.notAcceptingResponseMessage
    })

    this.selectedDateTime.set(!!this.formInfo()!.stopAcceptingResponseOn ? new Date(this.formInfo()!.stopAcceptingResponseOn) : null)

    this.formGroup.valueChanges.subscribe(() => this.setCanSave())
  }

  ngOnDestroy() {
    this.selectedDateEffect.destroy()
  }

  protected onDateTimeOkClick(menu: MatMenu) {
    this.dateTimeOkClickedOnce.set(true)
    this.formGroup.controls.time.markAsTouched()
    if (this.formGroup.controls.date.hasError('required')) {
      this.showDateError.set(true)
      return
    }

    const date = this.formGroup.value.date
    const time = this.formGroup.value.time
    if (!!date && !!time) {
      const dateTime = new Date()
      dateTime.setFullYear(date.getFullYear(), date.getMonth(), date.getDate())
      dateTime.setHours(time.getHours(), time.getMinutes(), time.getSeconds())
      this.selectedDateTime.set(dateTime)
      menu._setIsOpen(false)
    }
  }

  protected onSaveClick() {
    if (!this.canSave()) return

    const prevForm = this.formInfo()!

    this.editFormService.updateFormInfo(prevForm.id, {
      acceptingResponse: true,
      description: prevForm.description,
      notAcceptingResponseMessage: this.formGroup.value.notAcceptingResponseMessage ?? 'This form is no longer accepting responses.',
      published: prevForm.published,
      stopAcceptingResponseAfterResponse:
        this.formGroup.value.radioGroup === 'response-limit' ?
          this.formGroup.value.responseLimit! : null,
      stopAcceptingResponseOn:
        this.formGroup.value.radioGroup === 'date' ?
          this.selectedDateTime() : null,
      name: prevForm.name,
      title: prevForm.title
    }, () => this.dialogRef.close())
  }

  protected setCanSave() {
    const isSame = ObjectUtil.areMatchingFieldsSame(this.formInfo()!, {
      notAcceptingResponseMessage: this.formGroup.value.notAcceptingResponseMessage,
      stopAcceptingResponseOn: this.selectedDateTime(),
      stopAcceptingResponseAfterResponse: this.formGroup.value.responseLimit
    })

    const formValid = !!this.selectedDateTime() ||
      (
        this.formGroup.controls.radioGroup.value === 'response-limit' &&
        this.formGroup.controls.responseLimit.valid
      )

    this.canSave.set(!isSame && formValid)
  }
}
