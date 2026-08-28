import {Component, inject, OnInit, signal} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {FileUploadRes} from '../../../../model/edit-form/question/response/file-upload-res';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatSlideToggle} from '@angular/material/slide-toggle';
import {MatCheckbox} from '@angular/material/checkbox';
import {FileUploadConstant} from '../../../../constant/file-upload-constant';
import {MatFormField} from '@angular/material/input';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {FileType} from '../../../../type/file-type';
import {OnlyFileUploadAddUpdateReq} from '../../../../model/edit-form/question/request/file-upload-add-update-req';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-edit-form-file-upload',
  imports: [
    FormsModule,
    MatSlideToggle,
    ReactiveFormsModule,
    MatCheckbox,
    MatFormField,
    MatOption,
    MatSelect,
    MatButton,
    MatIcon,
  ],
  templateUrl: './edit-form-file-upload.html',
  styleUrl: './edit-form-file-upload.scss',
})
export class EditFormFileUpload extends EditFormQuestionComponent<FileUploadRes, OnlyFileUploadAddUpdateReq> implements OnInit {

  protected fileTypes = signal(FileUploadConstant.FILE_TYPES)
  protected allowedFileUploadSizes = signal(FileUploadConstant.ALLOWED_FILE_UPLOAD_SIZES)

  protected showFileCategories = signal<boolean>(false)

  protected selectedFileTypes = signal<FileType[]>([])

  protected formStateService = inject(EditFormStateService)

  protected formGroup = new FormGroup({
    fileUploadSize: new FormControl(this.allowedFileUploadSizes()[2], [Validators.required])
  })

  ngOnInit() {
    this.selectedFileTypes.set(this.question().allowedFileTypes)
    this.showFileCategories.set(!!this.question().allowedFileTypes.length)

    this.formGroup.patchValue({
      fileUploadSize: this.question().maxFileSize / 1024 / 1024
    })

    this.emitCanSaveHasError()

    this.formGroup.valueChanges.subscribe(val => {
      this.emitCanSaveHasError()
      this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
    })
    this.formGroup.statusChanges.subscribe(() => this.emitCanSaveHasError())
  }

  override getOnlyQuestionAddUpdateReq(): OnlyFileUploadAddUpdateReq {
    return {
      allowedFileCategories: this.selectedFileTypes().map(ft => ft.category).flat(),
      maxFileSize: this.formGroup.value.fileUploadSize! * 1024 * 1024
    }
  }

  protected onShowFileCategoriesCheckChange(checked: boolean) {
    if (!checked) {
      if (this.selectedFileTypes().length !== 0) {
        this.emitCanSaveHasError()
        this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
      }
      this.selectedFileTypes.set([])
    }
    this.showFileCategories.set(checked)
  }

  protected onFileTypeCheckChange(checked: boolean, fileType: FileType) {
    this.selectedFileTypes.update(prev => {
      if (checked) {
        return [...prev, {...fileType}]
      } else {
        return [...prev.filter(ft => ft.category !== fileType.category)]
      }
    })
    this.emitCanSaveHasError()
    this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
  }

  protected toCheckFileTypeCheckbox(fileType: FileType) {
    return !!this.selectedFileTypes().find(ft => ft.category === fileType.category)
  }

  private emitCanSaveHasError() {
    this.canSaveQuestion.emit(this.formGroup.valid)
    this.hasError.emit(this.formGroup.invalid)
  }

}
