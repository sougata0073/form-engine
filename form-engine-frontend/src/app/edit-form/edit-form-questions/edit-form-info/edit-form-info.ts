import { Component, inject, input, OnInit, signal } from '@angular/core';
import { QuestionCard } from '../../../shared/question-card/question-card';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { EditFormStateService } from '../../../service/edit-form-state-service';
import { FocusableComponent } from '../../../type/focusable-component';
import { EditFormService } from '../../../service/edit-form-service';
import { FormDetails } from '../../../model/form/form-details';
import { CdkTextareaAutosize } from '@angular/cdk/text-field';
import { FormUpdateReq } from '../../../model/form/form-update-req';

@Component({
  selector: 'app-edit-form-info',
  imports: [
    QuestionCard,
    ReactiveFormsModule,
    MatInput,
    MatFormField,
    MatLabel,
    CdkTextareaAutosize,
  ],
  templateUrl: './edit-form-info.html',
  styleUrl: './edit-form-info.scss',
})
export class EditFormInfo implements OnInit, FocusableComponent {
  formRes = input.required<FormDetails>();

  componentId = signal<string>('form-info');

  protected editFormStateService = inject(EditFormStateService);
  protected editFormService = inject(EditFormService);
  protected formStateService = inject(EditFormStateService);

  protected formGroup = new FormGroup({
    title: new FormControl<string | null>(null),
    description: new FormControl<string | null>(null),
  });

  ngOnInit() {
    this.formGroup.patchValue({
      title: this.formRes().formInfo.title,
      description: this.formRes().formInfo.description,
    });

    this.formGroup.controls.title.valueChanges.subscribe(() => {
      if (this.formGroup.invalid) return;

      this.editFormService.updateFormInfo(this.formRes().formInfo.id, {
        title: this.formGroup.getRawValue().title,
        updateFields: ['title' satisfies keyof FormUpdateReq],
      });
    });

    this.formGroup.controls.description.valueChanges.subscribe(() => {
      if (this.formGroup.invalid) return;

      this.editFormService.updateFormInfo(this.formRes().formInfo.id, {
        description: this.formGroup.getRawValue().description,
        updateFields: ['description' satisfies keyof FormUpdateReq],
      });
    });

    this.editFormStateService.changeFocus(this.componentId());
  }
}
