import { Component, inject } from '@angular/core';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatDivider } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { EditFormService } from '../../service/edit-form-service';
import { FormUpdateReq } from '../../model/form/form-update-req';
import { FormGeneralSettingsUpdateReq } from '../../model/form/form-general-settings-update-req';

@Component({
  selector: 'app-publish-form-dialog',
  imports: [
    MatDialogTitle,
    MatDivider,
    MatIcon,
    MatButton,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
  ],
  templateUrl: './publish-form-dialog.html',
  styleUrl: './publish-form-dialog.scss',
})
export class PublishFormDialog {
  protected editFormService = inject(EditFormService);
  protected dialogRef = inject(MatDialogRef<PublishFormDialog>);

  protected formInfo = this.editFormService.formInfo;

  protected onPublishClick() {
    this.editFormService.updateFormInfo(
      this.formInfo()!.id,
      {
        generalSettingsUpdateReq: {
          published: true,
          acceptingResponse: true,
          stopAcceptingResponseOn: null,
          responseLimit: null,
          updateFields: [
            'published' satisfies keyof FormGeneralSettingsUpdateReq,
            'acceptingResponse' satisfies keyof FormGeneralSettingsUpdateReq,
            'stopAcceptingResponseOn' satisfies keyof FormGeneralSettingsUpdateReq,
            'responseLimit' satisfies keyof FormGeneralSettingsUpdateReq,
          ],
        },
        updateFields: ['generalSettingsUpdateReq' satisfies keyof FormUpdateReq],
      },
      (res) => {
        this.dialogRef.close(res);
      },
    );
  }
}
