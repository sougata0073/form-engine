import {Component, input} from '@angular/core';

@Component({
  selector: 'app-all-responses-summary',
  imports: [],
  templateUrl: './all-responses-summary.html',
  styleUrl: './all-responses-summary.scss',
})
export class AllResponsesSummary {

  formId = input.required<string>()
  questionId = input.required<string>({alias: 'q'})

}
