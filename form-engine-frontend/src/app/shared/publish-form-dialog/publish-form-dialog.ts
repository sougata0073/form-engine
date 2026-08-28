import {Component, inject} from '@angular/core';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatDivider} from '@angular/material/list';
import {MatIcon} from '@angular/material/icon';
import {MatButton} from '@angular/material/button';
import {EditFormService} from '../../service/edit-form-service';

@Component({
  selector: 'app-publish-form-dialog',
  imports: [
    MatDialogTitle,
    MatDivider,
    MatIcon,
    MatButton,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose
  ],
  templateUrl: './publish-form-dialog.html',
  styleUrl: './publish-form-dialog.scss',
})
export class PublishFormDialog {

  protected editFormService = inject(EditFormService)
  protected dialogRef = inject(MatDialogRef<PublishFormDialog>)

  protected formInfo = this.editFormService.formInfo

  protected onPublishClick() {
    const prevForm = this.formInfo()!

    this.editFormService.updateFormInfo(prevForm.id, {
      name: prevForm.name,
      title: prevForm.title,
      description: prevForm.description,
      acceptingResponse: true,
      notAcceptingResponseMessage: prevForm.notAcceptingResponseMessage,
      published: true,
      stopAcceptingResponseOn: null,
      stopAcceptingResponseAfterResponse: null
    }, () => {
      this.dialogRef.close()
    })
  }

}
