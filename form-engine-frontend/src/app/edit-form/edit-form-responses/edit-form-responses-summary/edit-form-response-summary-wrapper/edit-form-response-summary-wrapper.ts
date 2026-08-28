import {Component, inject, input, OnInit, viewChild, ViewContainerRef} from '@angular/core';
import {FormResponseSummaryComponentFactory} from '../../../../service/form-response-summary-component-factory';
import {MatCard, MatCardContent} from '@angular/material/card';
import {AnyResponseSummaryRes} from '../../../../type/any-response-summary-res';

@Component({
  selector: 'app-edit-form-response-summary-wrapper',
  imports: [
    MatCard,
    MatCardContent
  ],
  templateUrl: './edit-form-response-summary-wrapper.html',
  styleUrl: './edit-form-response-summary-wrapper.scss',
})
export class EditFormResponseSummaryWrapper implements OnInit {

  formId = input.required<string>()
  responseSummary = input.required<AnyResponseSummaryRes>()

  private componentHost = viewChild('componentHost', {read: ViewContainerRef})

  private componentFactory = inject(FormResponseSummaryComponentFactory);

  ngOnInit() {
    this.componentFactory.getComponent(this.responseSummary().questionType).then(componentClass => {
      const compRef = this.componentHost()?.createComponent(componentClass);

      compRef?.setInput('formId', this.formId());
      compRef?.setInput('responseSummary', this.responseSummary());
    })
  }

}
