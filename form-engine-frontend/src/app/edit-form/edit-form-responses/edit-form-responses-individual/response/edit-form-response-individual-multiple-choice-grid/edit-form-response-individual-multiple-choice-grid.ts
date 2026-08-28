import {Component, signal} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {
  MultipleChoiceGridRes,
  OnlyMultipleChoiceGridColumnRes,
  OnlyMultipleChoiceGridRowRes
} from '../../../../../model/edit-form/question/response/multiple-choice-grid-res';
import {
  MultipleChoiceGridResponseIndividual
} from '../../../../../model/edit-form/responses/individual/multiple-choice-grid-response-individual';

type Option = {
  row: OnlyMultipleChoiceGridRowRes & { formControl: FormControl<string | null> },
  columns: OnlyMultipleChoiceGridColumnRes[]
}

@Component({
  selector: 'app-edit-form-response-individual-multiple-choice-grid',
  imports: [
    FormsModule,
    MatRadioButton,
    MatRadioGroup,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-multiple-choice-grid.html',
  styleUrl: './edit-form-response-individual-multiple-choice-grid.scss',
})
export class EditFormResponseIndividualMultipleChoiceGrid extends EditFormResponseIndividualComponent<MultipleChoiceGridRes, MultipleChoiceGridResponseIndividual> {

  protected options = signal<Option[]>([])

  protected formGroup = new FormGroup<Record<string, FormControl<string | null>>>({})

  override ngOnInit() {
    super.ngOnInit();

    const options = this.question().rows.map(r => {
      const option: Option = {
        row: {
          ...r,
          formControl: new FormControl<string | null>(null)
        },
        columns: this.question().columns.map(c => ({...c}))
      }

      this.formGroup.addControl(option.row.id, option.row.formControl)

      return option
    })

    const rowMap = new Map(this.response()?.rows.map(r => [r.rowId, r]))

    options.forEach(op => {
      op.row.formControl.disable()

      const row = rowMap.get(op.row.id)

      if (row) {
        op.row.formControl.setValue(row.columnId)
      }
    })

    this.options.set(options)
  }

}
