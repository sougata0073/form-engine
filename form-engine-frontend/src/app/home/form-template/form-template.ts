import {Component, input, output} from '@angular/core';
import {TemplateSummaryRes} from '../../model/template/template-summary-res';

@Component({
  selector: 'app-form-template',
  imports: [],
  templateUrl: './form-template.html',
  styleUrl: './form-template.scss',
})
export class FormTemplate {

  template = input.required<TemplateSummaryRes>()
  isRecentlyUsedTemplate = input.required<boolean>()
  photo = input<string>()

  onClick = output<void>()

}
