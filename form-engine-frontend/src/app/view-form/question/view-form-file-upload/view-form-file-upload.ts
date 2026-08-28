import {Component, inject, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {FileUploadRes} from '../../../model/edit-form/question/response/file-upload-res';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatError} from '@angular/material/input';
import {MatDialog} from '@angular/material/dialog';
import {SimpleDialog} from '../../../shared/simple-dialog/simple-dialog';
import {OnlyFileUploadResponsePutReq} from '../../../model/view-form/request/file-upload-response-put-req';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-view-form-file-upload',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatIcon,
    MatButton,
    MatError,
    MatIconButton,
    MatTooltip
  ],
  templateUrl: './view-form-file-upload.html',
  styleUrl: './view-form-file-upload.scss',
})
export class ViewFormFileUpload extends ViewFormQuestionComponent<FileUploadRes, OnlyFileUploadResponsePutReq> {

  protected selectedFile: File | null = null
  protected allowedFileMimes = signal<string[]>([])

  override formGroup = new FormGroup({
    file: new FormControl<string | null>(null)
  })

  private dialog = inject(MatDialog)

  override ngOnInit() {
    super.ngOnInit();

    this.allowedFileMimes.set(
      this.question().allowedFileTypes.map(ft => ft.mimeTypes).flat()
    )
  }

  override getOnlyQuestionResponsePutReq(): OnlyFileUploadResponsePutReq | null {
    const fileName = this.formGroup.value.file
    return !fileName ? null : {
      fileName: fileName,
      fileUrl: fileName,
      fileSize: this.selectedFile!.size,
      fileMimeType: this.selectedFile!.type
    }
  }

  override clearForm() {
    this.formGroup.controls.file.patchValue(null)
    this.selectedFile = null
  }

  protected onFileChange(fileList: FileList | null) {
    this.selectedFile = fileList?.item(0) ?? null
    if (!this.selectedFile) return

    const actualFileSizeInMb = (this.selectedFile.size / 1024 / 1024).toFixed(2)
    const maxFileSizeInMb = (this.question().maxFileSize / 1024 / 1024).toFixed(2)

    if (this.selectedFile.size > this.question().maxFileSize) {
      this.selectedFile = null
      this.formGroup.controls.file.patchValue(null)
      this.dialog.open(
        SimpleDialog, {
          data: SimpleDialog.configure(
            'Error',
            `Maximum allowed file size is ${maxFileSizeInMb} MB. Uploaded file size is ${actualFileSizeInMb} MB.`,
            'Ok'
          )
        }
      )
      return
    }
  }
}
