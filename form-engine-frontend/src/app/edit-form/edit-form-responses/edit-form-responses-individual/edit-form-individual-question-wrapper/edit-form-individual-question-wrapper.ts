import {
  Component,
  ComponentRef,
  inject,
  input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  viewChild,
  ViewContainerRef
} from '@angular/core';
import {FormResponseIndividualComponentFactory} from '../../../../service/form-response-individual-component-factory';
import {AnyQuestionRes} from '../../../../type/any-question-res';
import {AnyResponseIndividual} from '../../../../type/any-response-individual';
import {ReactiveFormsModule} from '@angular/forms';
import {QuestionCard} from '../../../../shared/question-card/question-card';

@Component({
  selector: 'app-edit-form-individual-question-wrapper',
  imports: [
    ReactiveFormsModule,
    QuestionCard
  ],
  templateUrl: './edit-form-individual-question-wrapper.html',
  styleUrl: './edit-form-individual-question-wrapper.scss',
})
export class EditFormIndividualQuestionWrapper implements OnInit, OnChanges, OnDestroy {

  question = input.required<AnyQuestionRes>()
  response = input<AnyResponseIndividual>()

  private componentHost = viewChild('componentHost', {read: ViewContainerRef})

  private componentFactory = inject(FormResponseIndividualComponentFactory);
  private createdComponentRef?: ComponentRef<unknown>

  private loadVersion = 0;

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    const responseChange = changes['response']

    if (responseChange) {
      const version = ++this.loadVersion;

      this.createdComponentRef?.destroy()
      this.createdComponentRef = undefined

      this.componentFactory.getComponent(this.question().questionType).then(componentClass => {

        if (version !== this.loadVersion) {
          return;
        }

        this.createdComponentRef = this.componentHost()?.createComponent(componentClass);

        this.createdComponentRef?.setInput('question', this.question());
        this.createdComponentRef?.setInput('response', this.response());
      })
    }
  }

  ngOnDestroy() {
    this.createdComponentRef?.destroy()
    this.createdComponentRef = undefined
  }

}
