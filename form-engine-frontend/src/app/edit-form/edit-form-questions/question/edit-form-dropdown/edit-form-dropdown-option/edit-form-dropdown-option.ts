import {Component, inject, input, OnInit, output} from '@angular/core';
import {MatFormField, MatInput} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {EditFormStateService} from '../../../../../service/edit-form-state-service';
import {DropdownOption} from '../../../../../type/dropdown-option';

@Component({
  selector: 'app-edit-form-dropdown-option',
  imports: [
    MatFormField,
    MatIcon,
    MatIconButton,
    MatInput,
    MatTooltip,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-dropdown-option.html',
  styleUrl: './edit-form-dropdown-option.scss',
})
export class EditFormDropdownOption implements OnInit {

  parentComponentId = input.required<string>()
  option = input.required<DropdownOption>()
  disableRemove = input<boolean>(false)

  optionTextChange = output<DropdownOption>()
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
