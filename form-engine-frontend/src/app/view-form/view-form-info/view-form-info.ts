import {Component, input} from '@angular/core';
import {QuestionCard} from '../../shared/question-card/question-card';
import {FormDetails} from '../../model/form/form-details';

@Component({
  selector: 'app-view-form-info',
  imports: [
    QuestionCard
  ],
  templateUrl: './view-form-info.html',
  styleUrl: './view-form-info.scss',
})
export class ViewFormInfo {

  formRes = input.required<FormDetails>()
}
