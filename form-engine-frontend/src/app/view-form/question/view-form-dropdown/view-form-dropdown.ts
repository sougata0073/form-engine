import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {DropdownRes} from '../../../model/edit-form/question/response/dropdown-res';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatError, MatFormField, MatLabel} from '@angular/material/input';
import {MatOption, MatSelect, MatSelectModule} from '@angular/material/select';
import {ValueLabelPair} from '../../../type/value-label-pair';
import {MatFormFieldModule} from '@angular/material/form-field';
import {OnlyDropdownResponsePutReq} from '../../../model/view-form/request/dropdown-response-put-req';

@Component({
  selector: 'app-view-form-dropdown',
  imports: [
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatSelect,
    MatOption,
    MatError,
    FormsModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './view-form-dropdown.html',
  styleUrl: './view-form-dropdown.scss',
})
export class ViewFormDropdown extends ViewFormQuestionComponent<DropdownRes, OnlyDropdownResponsePutReq> {

  protected options = signal<ValueLabelPair[]>([])

  override formGroup = new FormGroup({
    dropdown: new FormControl<string | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.options.update(() => {
      return this.question().options.map(op => {
        return {value: op.id, label: op.option}
      })
    })
  }

  override getOnlyQuestionResponsePutReq(): OnlyDropdownResponsePutReq | null {
    const id = this.formGroup.value.dropdown

    return id === null || id === undefined ? null : {
      responseOptionId: id
    }
  }
}
