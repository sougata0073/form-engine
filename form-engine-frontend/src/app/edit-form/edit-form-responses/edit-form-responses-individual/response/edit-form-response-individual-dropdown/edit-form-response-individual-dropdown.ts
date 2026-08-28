import {Component} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {DropdownRes} from '../../../../../model/edit-form/question/response/dropdown-res';
import {
  DropdownResponseIndividual
} from '../../../../../model/edit-form/responses/individual/dropdown-response-individual';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/input";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";

@Component({
  selector: 'app-edit-form-response-individual-dropdown',
  imports: [
    FormsModule,
    MatFormField,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-dropdown.html',
  styleUrl: './edit-form-response-individual-dropdown.scss',
})
export class EditFormResponseIndividualDropdown extends EditFormResponseIndividualComponent<DropdownRes, DropdownResponseIndividual> {

  protected formGroup = new FormGroup({
    dropdown: new FormControl<string | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.formGroup.controls.dropdown.disable()

    const responseOptionId = this.response()?.optionId

    if (responseOptionId) {
      this.formGroup.controls.dropdown.setValue(responseOptionId)
    }
  }

}
