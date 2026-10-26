import { Component, inject, OnInit, signal } from '@angular/core';
import {
  MatDialog,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatDivider } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import { MatMenu, MatMenuTrigger } from '@angular/material/menu';
import { CopyResponderLinkMenuContent } from '../copy-responder-link-menu-content/copy-responder-link-menu-content';
import { Router } from '@angular/router';
import { EditFormService } from '../../service/edit-form-service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatInput } from '@angular/material/input';
import { StopAcceptingResponseDialog } from '../stop-accepting-response-dialog/stop-accepting-response-dialog';
import { DatePipe } from '@angular/common';
import { FormUpdateReq } from '../../model/form/form-update-req';
import { FormGeneralSettingsUpdateReq } from '../../model/form/form-general-settings-update-req';

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
    DatePipe,
  ],
  templateUrl: './published-options-dialog.html',
  styleUrl: './published-options-dialog.scss',
})
export class PublishedOptionsDialog implements OnInit {
  protected router = inject(Router);
  protected editFormService = inject(EditFormService);
  protected dialogRef = inject(MatDialogRef<PublishedOptionsDialog>);
  protected dialog = inject(MatDialog);

  protected formInfo = this.editFormService.formInfo;

  protected editClicked = signal<boolean>(false);
  protected canSave = signal<boolean>(false);

  protected formGroup = new FormGroup({
    acceptingResponse: new FormControl<boolean>(false, { nonNullable: true }),
    notAcceptingResponseMessage: new FormControl<string | null>(null),
  });

  ngOnInit() {
    this.formGroup.patchValue({
      acceptingResponse: this.formInfo()!.generalSettings.acceptingResponse,
      notAcceptingResponseMessage: this.formInfo()!.generalSettings.notAcceptingResponseMessage,
    });

    this.formGroup.valueChanges.subscribe((val) => {
      const isSame =
        this.formInfo()!.generalSettings.acceptingResponse === val.acceptingResponse &&
        this.formInfo()!.generalSettings.notAcceptingResponseMessage ===
          val.notAcceptingResponseMessage;

      this.canSave.set(!isSame);
    });

    this.formGroup.controls.acceptingResponse.valueChanges.subscribe(() => {
      this.editClicked.set(false);
    });
  }

  protected onSaveClick() {
    if (!this.canSave()) return;

    const acceptingResponse = this.formGroup.getRawValue().acceptingResponse;
    const notAcceptingResponseMessage = this.formGroup.getRawValue().notAcceptingResponseMessage;

    this.editFormService.updateFormInfo(
      this.formInfo()!.id,
      {
        generalSettingsUpdateReq: {
          acceptingResponse: acceptingResponse,
          notAcceptingResponseMessage: acceptingResponse ? null : notAcceptingResponseMessage,
          updateFields: [
            'acceptingResponse' satisfies keyof FormGeneralSettingsUpdateReq,
            'notAcceptingResponseMessage' satisfies keyof FormGeneralSettingsUpdateReq,
          ],
        },
        updateFields: ['generalSettingsUpdateReq' satisfies keyof FormUpdateReq],
      },
      () => {
        this.dialogRef.close();
      },
    );
  }

  protected openStopAcceptingResponseDialog() {
    const ref = this.dialog.open(StopAcceptingResponseDialog);

    ref.afterOpened().subscribe(() => {
      this.dialogRef.close();
    });

    ref.afterClosed().subscribe(() => {
      this.dialog
        .open(PublishedOptionsDialog)
        .afterClosed()
        .subscribe(() => {
          this.dialogRef.close();
        });
    });
  }
}
