import {Component, inject, input, OnInit, signal} from '@angular/core';
import {QuestionCard} from '../../../shared/question-card/question-card';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {EditFormStateService} from '../../../service/edit-form-state-service';
import {FocusableComponent} from '../../../type/focusable-component';
import {EditFormService} from '../../../service/edit-form-service';
import {FormDetails} from '../../../model/form/form-details';
import {CdkTextareaAutosize} from '@angular/cdk/text-field';

@Component({
  selector: 'app-edit-form-info',
  imports: [
    QuestionCard,
    ReactiveFormsModule,
    MatInput,
    MatFormField,
    MatLabel,
    CdkTextareaAutosize
  ],
  templateUrl: './edit-form-info.html',
  styleUrl: './edit-form-info.scss',
})
export class EditFormInfo implements OnInit, FocusableComponent {

  formRes = input.required<FormDetails>()

  componentId = signal<string>("form-info")

  protected editFormStateService = inject(EditFormStateService)
  protected editFormService = inject(EditFormService)
  protected formStateService = inject(EditFormStateService)

  protected formGroup = new FormGroup({
    title: new FormControl<string | null>(''),
    description: new FormControl<string | null>('')
  })

  ngOnInit() {
    this.formGroup.patchValue({
      title: this.formRes().title,
      description: this.formRes().description
    })

    this.formGroup.valueChanges.subscribe(val => {
      if (this.formGroup.invalid) return

      this.editFormService.updateFormInfo(this.formRes().id, {
        name: this.formRes().name,
        title: val.title!,
        description: val.description!,
        acceptingResponse: this.formRes().acceptingResponse,
        notAcceptingResponseMessage: this.formRes().notAcceptingResponseMessage,
        published: this.formRes().published,
        stopAcceptingResponseAfterResponse: this.formRes().stopAcceptingResponseAfterResponse,
        stopAcceptingResponseOn: !!this.formRes().stopAcceptingResponseOn ? new Date(this.formRes().stopAcceptingResponseOn) : null
      })
    })

    this.editFormStateService.changeFocus(this.componentId())
  }

}
