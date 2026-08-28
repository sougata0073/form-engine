import {Component, signal} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {
  CheckboxResponseIndividual
} from '../../../../../model/edit-form/responses/individual/checkbox-response-individual';
import {CheckboxRes, OnlyCheckboxOptionRes} from '../../../../../model/edit-form/question/response/checkbox-res';
import {AnyCheckboxValidationConfig} from '../../../../../type/any-checkbox-validation-config';
import {MatCheckbox} from '@angular/material/checkbox';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-edit-form-response-individual-checkbox',
  imports: [
    MatCheckbox,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-checkbox.html',
  styleUrl: './edit-form-response-individual-checkbox.scss',
})
export class EditFormResponseIndividualCheckbox extends EditFormResponseIndividualComponent<CheckboxRes<AnyCheckboxValidationConfig>, CheckboxResponseIndividual> {

  options = signal<(OnlyCheckboxOptionRes & { formControl: FormControl<boolean> })[]>([])

  formGroup = new FormGroup({
    options: new FormArray<FormControl<boolean>>([])
  })

  override ngOnInit() {
    super.ngOnInit();

    const options = this.question().options.map(option => {
      const isSelected = this.response()?.optionIds.includes(option.id) ?? false
      const formControl = new FormControl<boolean>(isSelected, {nonNullable: true})

      formControl.disable();

      return {...option, formControl: formControl}
    })

    this.options.set(options)
  }

}
