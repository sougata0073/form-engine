import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {MultipleChoiceRes} from '../../../model/edit-form/question/response/multiple-choice-res';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {ValueLabelPair} from '../../../type/value-label-pair';
import {MatButton} from '@angular/material/button';
import {OnlyMultipleChoiceResponsePutReq} from '../../../model/view-form/request/multiple-choice-response-put-req';

@Component({
  selector: 'app-view-form-multiple-choice',
  imports: [
    ReactiveFormsModule,
    MatRadioGroup,
    MatRadioButton,
    MatButton
  ],
  templateUrl: './view-form-multiple-choice.html',
  styleUrl: './view-form-multiple-choice.scss',
})
export class ViewFormMultipleChoice extends ViewFormQuestionComponent<MultipleChoiceRes, OnlyMultipleChoiceResponsePutReq> {

  protected options = signal<ValueLabelPair[]>([])

  override formGroup = new FormGroup({
    multipleChoice: new FormControl<string | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.options.set(
      this.question().options.map(op => {
        return {value: op.id, label: op.option}
      })
    )
  }

  override getOnlyQuestionResponsePutReq(): OnlyMultipleChoiceResponsePutReq | null {
    const id = this.formGroup.value.multipleChoice

    return id === null || id === undefined ? null : {
      responseOptionId: id
    }
  }

  protected onRadioButtonCLick(value: string) {
    if (!this.question().required && this.formGroup.value.multipleChoice === value) {
      this.formGroup.controls.multipleChoice.patchValue(null)
      this.hasError.emit(this.formGroup.invalid)
    }
  }

}
