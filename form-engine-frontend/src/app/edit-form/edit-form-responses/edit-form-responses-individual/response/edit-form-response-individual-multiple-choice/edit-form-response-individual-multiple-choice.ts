import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {MultipleChoiceRes} from "../../../../../model/edit-form/question/response/multiple-choice-res";
import {
  MultipleChoiceResponseIndividual
} from '../../../../../model/edit-form/responses/individual/multiple-choice-response-individual';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";

@Component({
  selector: 'app-edit-form-response-individual-multiple-choice',
  imports: [
    FormsModule,
    MatRadioButton,
    MatRadioGroup,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-multiple-choice.html',
  styleUrl: './edit-form-response-individual-multiple-choice.scss',
})
export class EditFormResponseIndividualMultipleChoice extends EditFormResponseIndividualComponent<MultipleChoiceRes, MultipleChoiceResponseIndividual> {

  protected formGroup = new FormGroup({
    multipleChoice: new FormControl<string | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.formGroup.controls.multipleChoice.disable()

    const selectedOption = this.response()?.optionId

    if (selectedOption) {
      this.formGroup.controls.multipleChoice.setValue(selectedOption)
    }

  }

}
