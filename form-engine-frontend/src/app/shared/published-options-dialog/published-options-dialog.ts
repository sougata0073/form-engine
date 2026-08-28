import {Component, inject, OnInit, signal} from '@angular/core';
import {MatDialog, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {MatDivider} from '@angular/material/list';
import {MatIcon} from '@angular/material/icon';
import {MatButton} from '@angular/material/button';
import {MatSlideToggle} from '@angular/material/slide-toggle';
import {MatMenu, MatMenuTrigger} from '@angular/material/menu';
import {CopyResponderLinkMenuContent} from '../copy-responder-link-menu-content/copy-responder-link-menu-content';
import {Router} from '@angular/router';
import {EditFormService} from '../../service/edit-form-service';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatFormField, MatInput} from '@angular/material/input';
import {StopAcceptingResponseDialog} from '../stop-accepting-response-dialog/stop-accepting-response-dialog';
import {DatePipe} from '@angular/common';
import {ObjectUtil} from '../../util/object-util';

@Component({
  selector: 'app-published-options-dialog',
  imports: [
    MatDialogTitle,
    MatDivider,
    MatDialogContent,
    MatIcon,
    MatButton,
    MatSlideToggle,
    MatDialogClose,
    MatMenu,
    CopyResponderLinkMenuContent,
    MatMenuTrigger,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    DatePipe
  ],
  templateUrl: './published-options-dialog.html',
  styleUrl: './published-options-dialog.scss',
})
export class PublishedOptionsDialog implements OnInit {

  protected router = inject(Router)
  protected editFormService = inject(EditFormService)
  protected dialogRef = inject(MatDialogRef<PublishedOptionsDialog>)
  protected dialog = inject(MatDialog)

  protected formInfo = this.editFormService.formInfo

  protected editClicked = signal<boolean>(false)
  protected canSave = signal<boolean>(false)

  protected formGroup = new FormGroup({
    acceptingResponse: new FormControl<boolean>(false),
    notAcceptingResponseMessage: new FormControl<string | null>(null)
  })

  ngOnInit() {
    this.formGroup.patchValue({
      acceptingResponse: this.formInfo()!.acceptingResponse,
      notAcceptingResponseMessage: this.formInfo()!.notAcceptingResponseMessage
    })

    this.formGroup.valueChanges.subscribe(val => {
      const canSave = !ObjectUtil.areMatchingFieldsSame(this.formInfo()!, val)
      this.canSave.set(canSave)
    })

    this.formGroup.controls.acceptingResponse.valueChanges.subscribe(() => {
      this.editClicked.set(false)
    })
  }

  protected onSaveClick() {
    if (!this.canSave()) return

    const prevForm = this.formInfo()!
    const acceptingResponse = this.formGroup.value.acceptingResponse

    this.editFormService.updateFormInfo(prevForm.id, {
      name: prevForm.name,
      title: prevForm.title,
      description: prevForm.description,
      published: prevForm.published,
      acceptingResponse: acceptingResponse ?? false,
      notAcceptingResponseMessage: this.formGroup.value.notAcceptingResponseMessage ?? 'This form is no longer accepting responses.',
      stopAcceptingResponseOn: acceptingResponse ? (!!prevForm.stopAcceptingResponseOn ? new Date(prevForm.stopAcceptingResponseOn) : null) : null,
      stopAcceptingResponseAfterResponse: acceptingResponse ? prevForm.stopAcceptingResponseAfterResponse : null
    }, () => {
      this.dialogRef.close()
    })
  }

  protected openStopAcceptingResponseDialog() {
    const ref = this.dialog.open(StopAcceptingResponseDialog)

    ref.afterOpened().subscribe(() => {
      this.dialogRef.close()
    })

    ref.afterClosed().subscribe(() => {
      this.dialog.open(PublishedOptionsDialog).afterClosed().subscribe(() => {
        this.dialogRef.close()
      })
    })
  }

}
