import {Directive, inject, input, OnInit, signal} from '@angular/core';
import {ResponseQuestion} from '../model/edit-form/responses/question/response-question';
import {Router} from '@angular/router';
import {ResponseQuestionSummary} from '../model/edit-form/responses/question/response-question-summary';
import {FormResponseService} from '../service/form-response-service';
import {CommonResponseQuestionResponse} from '../model/edit-form/responses/question/common-response-question-response';
import {QuestionRes} from '../model/edit-form/question/response/question-res';

@Directive()
export class EditFormResponseQuestionComponent<
  R extends ResponseQuestion<ResQRes>,
  ResQRes extends CommonResponseQuestionResponse,
  QDetails extends QuestionRes
> implements OnInit {

  formId = input.required<string>()
  questionDetails = input.required<QDetails>();

  response = signal<R | null>(null);

  protected listStyle = signal<Record<string, string>>({'display': 'flex', 'flex-direction': 'column', 'gap': '12px'})
  protected isNextPageLoading = signal<boolean>(false)
  protected hasMoreItems: boolean = true
  protected pageNumber: number = 0
  protected readonly pageSize: number = 20

  extraParam = signal<Record<string, string>>({})

  protected router = inject(Router)
  protected formResponseService = inject(FormResponseService)

  ngOnInit() {
  }

  loadNextPage(resetPreviousResponse?: boolean) {
    if (this.isNextPageLoading() || !this.hasMoreItems) return

    this.isNextPageLoading.set(true)

    if (resetPreviousResponse) {
      this.response.set(null)
    }

    this.formResponseService.getResponseByQuestion<R>(
      this.formId(),
      this.questionDetails().id,
      this.pageNumber,
      this.pageSize,
      this.extraParam(),
      res => {
        this.isNextPageLoading.set(false)

        if (res.responses.length) {
          this.pageNumber++

          if (res.responses.length < this.pageSize) {
            this.hasMoreItems = false
          }

          if (resetPreviousResponse) {
            this.response.set(res)
          } else {
            this.response.update(prev => {
              if (prev) {
                return {...prev, responses: [...prev.responses, ...res.responses]}
              }
              return res
            })
          }
        } else {
          this.hasMoreItems = false
        }
      }
    )
  }

}
