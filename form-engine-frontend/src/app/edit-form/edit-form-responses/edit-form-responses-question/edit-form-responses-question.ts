import {Component, inject, input, OnInit, signal} from '@angular/core';
import {FormResponseService} from '../../../service/form-response-service';
import {ActivatedRoute, Router} from '@angular/router';
import {
  EditFormResponseQuestionWrapper
} from './edit-form-response-question-wrapper/edit-form-response-question-wrapper';
import {AnyResponseQuestionSummary} from '../../../type/any-response-question-summary';
import {MatFormField} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatOption, MatSelect} from '@angular/material/select';
import {QuestionSummariesRes} from '../../../model/question/question-summaries-res';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {AnyQuestionRes} from '../../../type/any-question-res';
import {QuestionService} from '../../../service/question-service';
import {FormDetails} from '../../../model/form/form-details';
import {FormService} from '../../../service/form-service';
import { FormResponseServiceShared } from '../../../service/form-response-service-shared';

@Component({
  selector: 'app-edit-form-responses-question',
  imports: [
    EditFormResponseQuestionWrapper,
    MatFormField,
    ReactiveFormsModule,
    MatSelect,
    MatOption,
    MatPaginator,
    MatCardContent,
    MatCard,
    MatProgressSpinner,


  ],
  templateUrl: './edit-form-responses-question.html',
  styleUrl: './edit-form-responses-question.scss',
})
export class EditFormResponsesQuestion implements OnInit {

  questionId = input.required<string>({alias: 'q'})

  protected formId = signal<string | null>(null)
  protected formDetails = signal<FormDetails | null>(null)
  protected questionDetails = signal<AnyQuestionRes | null>(null)

  private formResponseServiceShared = inject(FormResponseServiceShared)
  private formService = inject(FormService)
  private activatedRoute = inject(ActivatedRoute)
  private router = inject(Router)

  protected questionSelectorPaginatorIndex = signal<number>(0)

  protected formGroup = new FormGroup({
    questionSelector: new FormControl<string>('')
  })

  ngOnInit() {

    this.activatedRoute.parent!.parent!.paramMap.subscribe(params => {
      this.formId.set(params.get('formId')!);

      this.formService.getFormDetails(this.formId()!, res => {
        this.formDetails.set(res)

        const urlQuestionId = this.activatedRoute.snapshot.queryParams['q']

        if (urlQuestionId) {
          const index = res.questions.findIndex(q => q.id === urlQuestionId)
          if (index !== -1) {
            this.loadResponse(urlQuestionId, index)
          }
        } else {
          const firstQuestion = res.questions.at(0)
          if (firstQuestion) {
            this.loadResponse(firstQuestion.id, 0)
          }
        }
      })

      this.formResponseServiceShared.getFormResponseCount(this.formId()!, res => {

      })
    })

    this.formGroup.controls.questionSelector.valueChanges.subscribe(val => {
      const index = this.formDetails()?.questions.findIndex(q => q.id === val)
      if (val && index !== undefined && index !== -1) {
        this.loadResponse(val, index)
      }
    })
  }

  handleQuestionSelectorPaginatorEvent(e: PageEvent) {
    const question = this.formDetails()?.questions.at(e.pageIndex)
    if (question) {
      this.loadResponse(question.id, e.pageIndex)
    }
  }

  private loadResponse(questionId: string, paginatorIndex: number) {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: {q: questionId}
    })
    this.questionSelectorPaginatorIndex.set(paginatorIndex)
    this.formGroup.controls.questionSelector.setValue(questionId, {emitEvent: false})

    this.questionDetails.set(this.formDetails()?.questions.find(q => q.id === questionId) ?? null)
  }

}
