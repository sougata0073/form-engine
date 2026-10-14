import {
  Component,
  ComponentRef,
  inject,
  input,
  OnChanges,
  OnInit,
  output,
  signal,
  SimpleChanges,
  viewChild,
  ViewContainerRef,
} from '@angular/core';
import { QuestionCard } from '../../shared/question-card/question-card';
import { AnyQuestionRes } from '../../type/any-question-res';
import { QuestionType } from '../../type/question-type';
import { ViewFormQuestionComponentFactory } from '../../service/view-form-question-component-factory';
import { ViewFormQuestionComponent } from '../../type/view-form-question-component';
import { Observable } from 'rxjs';
import { AnyQuestionResponsePutReq } from '../../type/any-question-response-put-req';
import { QuestionResponsePutReq } from '../../model/view-form/request/question-response-put-req';

@Component({
  selector: 'app-view-form-question-wrapper',
  imports: [QuestionCard],
  templateUrl: './view-form-question-wrapper.html',
  styleUrl: './view-form-question-wrapper.scss',
})
export class ViewFormQuestionWrapper implements OnInit, OnChanges {
  question = input.required<AnyQuestionRes>();
  submitClick = input<Observable<boolean>>();
  clearClick = input<Observable<boolean>>();

  questionResponse = output<AnyQuestionResponsePutReq>();

  componentId = signal<string>(crypto.randomUUID());
  hasError = signal<boolean>(false);

  private questionHost = viewChild('questionHost', { read: ViewContainerRef });
  private createdComponentRef?: ComponentRef<unknown>;

  private componentFactory = inject(ViewFormQuestionComponentFactory);

  ngOnInit() {
    this.createComponent(this.question().questionType, this.question());

    this.submitClick()?.subscribe((val) => {
      const instance = this.createdComponentRef?.instance;
      if (val && instance instanceof ViewFormQuestionComponent) {
        const onlyQuestionResponse = instance.getOnlyQuestionResponsePutReq();

        const questionResponse: QuestionResponsePutReq = {
          questionId: this.question().id,
          questionType: this.question().questionType,
        };

        if (onlyQuestionResponse) {
          this.questionResponse.emit({
            ...onlyQuestionResponse,
            ...questionResponse,
          });
        }
      }
    });

    this.clearClick()?.subscribe((val) => {
      const instance = this.createdComponentRef?.instance;
      if (val && instance instanceof ViewFormQuestionComponent) {
        instance.clearForm();
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    const questionChange = changes['question']
    if(!questionChange.isFirstChange()) {
      this.createdComponentRef?.setInput('question', this.question());
    }
  }

  private async createComponent(questionType: QuestionType, question: AnyQuestionRes) {
    this.removeComponent();

    const componentClass = await this.componentFactory.getComponent(questionType);
    this.createdComponentRef = this.questionHost()?.createComponent(componentClass);

    this.createdComponentRef?.setInput('question', question);
    this.createdComponentRef?.setInput('parentComponentId', this.componentId());

    const instance = this.createdComponentRef?.instance;
    if (instance instanceof ViewFormQuestionComponent) {
      instance.hasError.subscribe((val) => this.hasError.set(val));
    }
  }

  private removeComponent() {
    this.createdComponentRef?.destroy();
    this.createdComponentRef = undefined;
  }
}
