import {Component, inject, OnInit, signal} from '@angular/core';
import {EditFormQuestionComponent} from '../../../../type/edit-form-question-component';
import {LinearScaleRes} from '../../../../model/edit-form/question/response/linear-scale-res';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {NumberValidator} from '../../../../formValidator/number-validator';
import {FormGroupValidator} from '../../../../formValidator/form-group-validator';
import {MatError, MatFormField} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {EditFormStateService} from '../../../../service/edit-form-state-service';
import {MatIcon} from '@angular/material/icon';
import {ArrayUtil} from '../../../../util/array-util';
import {OnlyLinearScaleAddUpdateReq} from '../../../../model/edit-form/question/request/linear-scale-add-update-req';

@Component({
  selector: 'app-edit-form-linear-scale',
  imports: [
    MatFormField,
    MatSelect,
    ReactiveFormsModule,
    MatOption,
    MatError,
    MatIcon
  ],
  templateUrl: './edit-form-linear-scale.html',
  styleUrl: './edit-form-linear-scale.scss',
})
export class EditFormLinearScale extends EditFormQuestionComponent<LinearScaleRes, OnlyLinearScaleAddUpdateReq> implements OnInit {

  protected fromNumbers = signal<number[]>(ArrayUtil.fillByNumbers(1, 10))
  protected toNumbers = signal<number[]>(ArrayUtil.fillByNumbers(1, 10))
  protected previewNumbers = signal<number[]>([])

  protected formGroup = new FormGroup({
    fromNumber: new FormControl(1, [Validators.required, NumberValidator.between(1, 10)]),
    toNumber: new FormControl(5, [Validators.required, NumberValidator.between(1, 10)])
  }, {
    validators: [FormGroupValidator.rangeInputs('fromNumber', 'toNumber')]
  })

  protected formStateService = inject(EditFormStateService)

  ngOnInit() {
    this.formGroup.patchValue({
      fromNumber: this.question().fromNumber,
      toNumber: this.question().toNumber
    })
    this.previewNumbers.update(() => ArrayUtil.fillByNumbers(this.question().fromNumber, this.question().toNumber))

    this.emitCanSaveHasError()

    this.formGroup.valueChanges.subscribe(val => {
      this.previewNumbers.update(() => ArrayUtil.fillByNumbers(val.fromNumber ?? 0, val.toNumber ?? 0))

      this.emitCanSaveHasError()
      this.updateQuestion.emit(this.getOnlyQuestionAddUpdateReq())
    })
    this.formGroup.statusChanges.subscribe(() => this.emitCanSaveHasError())
  }

  override getOnlyQuestionAddUpdateReq(): OnlyLinearScaleAddUpdateReq {
    return {
      fromNumber: this.formGroup.value.fromNumber!,
      toNumber: this.formGroup.value.toNumber!
    }
  }

  protected isNotFormValid(): string | null {
    const fg = this.formGroup
    if (fg.hasError('rangeInputs')) {
      return `From number must be greater than to number`
    }
    return null
  }

  protected isNotFromNumberValid(): string | null {
    const fromNumber = this.formGroup.controls.fromNumber
    if (fromNumber.hasError('between')) {
      return 'From number must be between 1 and 10'
    }
    return null
  }

  protected isNotToNumberValid(): string | null {
    const toNumber = this.formGroup.controls.toNumber
    if (toNumber.hasError('between')) {
      return 'To number must be between 1 and 10'
    }
    return null
  }

  private emitCanSaveHasError() {
    this.canSaveQuestion.emit(this.formGroup.valid)
    this.hasError.emit(this.formGroup.invalid)
  }

}
