import {Component, inject, input, OnInit, output} from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormField, MatInput} from '@angular/material/input';
import {MatIconButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';
import {EditFormStateService} from '../../../../../service/edit-form-state-service';
import {CheckboxOption} from '../../../../../type/checkbox-option';

@Component({
  selector: 'app-edit-form-checkbox-option',
  imports: [
    MatIcon,
    FormsModule,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatIconButton,
    MatTooltip,
  ],
  templateUrl: './edit-form-checkbox-option.html',
  styleUrl: './edit-form-checkbox-option.scss',
})
export class EditFormCheckboxOption implements OnInit {

  parentComponentId = input.required<string>()
  option = input.required<CheckboxOption>()
  disableRemove = input<boolean>(false)

  optionTextChange = output<CheckboxOption>()
  remove = output<void>()
  canSaveQuestion = output<boolean>()

  protected formStateService = inject(EditFormStateService)

  protected formGroup = new FormGroup({
    option: new FormControl<string>('', {validators: [Validators.required]})
  })

  ngOnInit() {

    this.formGroup.patchValue({
      option: this.option().option
    })

    this.formGroup.valueChanges.subscribe(val => {
      this.canSaveQuestion.emit(this.formGroup.valid)
      this.optionTextChange.emit({...this.option(), option: val.option!})
    })

  }

}
