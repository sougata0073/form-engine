import {
  Component,
  ComponentRef,
  inject,
  input,
  OnChanges,
  OnInit,
  SimpleChanges,
  viewChild,
  ViewContainerRef
} from '@angular/core';
import {FormResponseQuestionComponentFactory} from '../../../../service/form-response-question-component-factory';
import {ResponseQuestionSummary} from '../../../../model/edit-form/responses/question/response-question-summary';
import {AnyQuestionRes} from '../../../../type/any-question-res';

@Component({
  selector: 'app-edit-form-response-question-wrapper',
  imports: [],
  templateUrl: './edit-form-response-question-wrapper.html',
  styleUrl: './edit-form-response-question-wrapper.scss',
})
export class EditFormResponseQuestionWrapper implements OnInit, OnChanges {

  formId = input.required<string>()
  questionDetails = input.required<AnyQuestionRes>()

  private componentHost = viewChild('componentHost', {read: ViewContainerRef})

  private componentFactory = inject(FormResponseQuestionComponentFactory);
  private createdComponentRef?: ComponentRef<unknown>

  private loadVersion = 0;

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    const questionDetailsChange = changes['questionDetails']

    if (questionDetailsChange) {

      const version = ++this.loadVersion;

      this.createdComponentRef?.destroy()
      this.createdComponentRef = undefined

      this.componentFactory.getComponent(this.questionDetails().questionType).then(componentClass => {

        if (version !== this.loadVersion) {
          return;
        }

        this.createdComponentRef = this.componentHost()?.createComponent(componentClass);

        this.createdComponentRef?.setInput('formId', this.formId());
        this.createdComponentRef?.setInput('questionDetails', this.questionDetails());
      })
    }
  }

}
