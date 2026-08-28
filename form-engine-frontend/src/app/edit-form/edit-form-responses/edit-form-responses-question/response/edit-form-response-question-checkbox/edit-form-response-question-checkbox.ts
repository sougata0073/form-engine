import {Component, signal} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  CheckboxResponseQuestionRes,
  CheckboxResponseQuestionResResponse,
  CheckboxResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/checkbox-response-question-res';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCheckbox} from '@angular/material/checkbox';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {CheckboxRes} from '../../../../../model/edit-form/question/response/checkbox-res';
import {AnyCheckboxValidationConfig} from '../../../../../type/any-checkbox-validation-config';

@Component({
  selector: 'app-edit-form-response-question-checkbox',
  imports: [
    MatCard,
    MatCardContent,
    MatButton,
    MatIcon,
    ReactiveFormsModule,
    MatCheckbox,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-checkbox.html',
  styleUrl: './edit-form-response-question-checkbox.scss',
})
export class EditFormResponseQuestionCheckbox extends EditFormResponseQuestionComponent<CheckboxResponseQuestionRes, CheckboxResponseQuestionResResponse, CheckboxRes<AnyCheckboxValidationConfig>> {

  protected isOptionsVisible = signal<boolean>(false)

  getOptionFromId(id: string) {
    return this.questionDetails().options.find(op => op.id === id);
  }

}
