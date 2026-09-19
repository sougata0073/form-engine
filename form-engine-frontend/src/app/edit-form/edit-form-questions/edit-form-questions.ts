import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {EditFormInfo} from './edit-form-info/edit-form-info';
import {EditFormService} from '../../service/edit-form-service';
import {MatIcon} from '@angular/material/icon';
import {MatFabButton} from '@angular/material/button';
import {ActivatedRoute} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {DefaultQuestionAddReq} from '../../constant/default-question-add-req';
import {QuestionAddReq} from '../../model/edit-form/question/addreq/question-add-req';
import {AnyOnlyQuestionAddUpdateReq} from '../../type/any-only-question-add-update-req';
import {AnyQuestionAddReq} from '../../type/any-question-add-req';
import {EditFormStateService} from '../../service/edit-form-state-service';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {EditFormQuestionWrapper} from './edit-form-question-wrapper/edit-form-question-wrapper';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {CdkScrollable} from '@angular/cdk/overlay';
import {AnyQuestionRes} from '../../type/any-question-res';
import {QuestionRes} from '../../model/edit-form/question/response/question-res';

@Component({
  selector: 'app-edit-form-questions',
  imports: [
    EditFormInfo,
    MatIcon,
    MatFabButton,
    MatProgressSpinner,
    EditFormQuestionWrapper,
    CdkDrag,
    CdkDropList,
  ],
  templateUrl: './edit-form-questions.html',
  styleUrl: './edit-form-questions.scss'
})
export class EditFormQuestions implements OnInit, OnDestroy {

  protected formId = signal<string>('')

  protected editFormService = inject(EditFormService)
  private title = inject(Title)
  protected activatedRoute = inject(ActivatedRoute)

  protected editFormStateService = inject(EditFormStateService)

  protected formRes = this.editFormService.formRes

  ngOnInit() {
    this.activatedRoute.parent!.paramMap.subscribe(params => {

      this.formId.set(params.get('formId')!);

      this.editFormService.loadFormRes(this.formId(), (res) => {

        let formTitle = this.formRes()!.title

        if (formTitle) {
          formTitle += ' - Form engine'
        } else {
          formTitle = 'Form engine'
        }
        this.title.setTitle(formTitle)

        this.activatedRoute.queryParams.subscribe(val => {
          if (val['publishedOptions']) {

          }
        })
      })
    })
  }

  ngOnDestroy() {
    
  }

  protected onAddQuestionClick() {
    this.editFormStateService.isFormGettingModified.set(true)

    const questionAddUpdateReq: QuestionAddReq = {
      question: null,
      description: null,
      required: false,
      questionType: 'SHORT_ANSWER'
    }
    const onlyQuestionAddUpdateReq: AnyOnlyQuestionAddUpdateReq =
      DefaultQuestionAddReq.get('SHORT_ANSWER')

    const question: AnyQuestionAddReq = {
      ...questionAddUpdateReq,
      ...structuredClone(onlyQuestionAddUpdateReq)
    }

    this.editFormService.addQuestion(question, res => {
      this.editFormStateService.isFormGettingModified.set(false)
    })
  }

  protected handleDropListDropEvent(e: CdkDragDrop<QuestionRes>) {
    if (e.previousIndex !== e.currentIndex) {
      this.editFormService.updateQuestionIndex(e.item.data.id, e.previousIndex, e.currentIndex)
    }
  }
}
