import {
  AfterViewInit,
  Component,
  ComponentRef,
  effect,
  ElementRef,
  inject,
  input,
  OnDestroy,
  OnInit,
  signal,
  viewChild,
  ViewContainerRef
} from '@angular/core';
import {QuestionType} from '../../../type/question-type';
import {QuestionCard} from '../../../shared/question-card/question-card';
import {EditFormStateService} from '../../../service/edit-form-state-service';
import {AnyQuestionRes} from '../../../type/any-question-res';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatDivider} from '@angular/material/list';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {MatSlideToggle} from '@angular/material/slide-toggle';
import {MatTooltip} from '@angular/material/tooltip';
import {QUESTION_TYPE_GROUPS} from '../../../constant/question-type-groups';
import {EditQuestionConstant} from '../../../constant/edit-question-constant';
import {EditQuestionMoreMenuItem} from '../../../type/edit-question-more-menu-item';
import {EditFormQuestionComponent} from '../../../type/edit-form-question-component';
import {EditFormService} from '../../../service/edit-form-service';
import {EditFormQuestionComponentFactory} from '../../../service/edit-form-question-component-factory';
import {AnyQuestionAddUpdateReq} from '../../../type/any-question-add-update-req';
import {QuestionAddUpdateReq} from '../../../model/edit-form/question/request/question-add-update-req';
import {AnyOnlyQuestionAddUpdateReq} from '../../../type/any-only-question-add-update-req';
import {DefaultQuestionAddUpdateReq} from '../../../constant/default-question-add-update-req';
import {CdkDragHandle} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-edit-form-question-wrapper',
  imports: [
    QuestionCard,
    FormsModule,
    MatDivider,
    MatFormField,
    MatIcon,
    MatIconButton,
    MatInput,
    MatLabel,
    MatMenu,
    MatMenuItem,
    MatOption,
    MatSelect,
    MatSlideToggle,
    MatTooltip,
    ReactiveFormsModule,
    MatMenuTrigger,
    CdkDragHandle,
  ],
  templateUrl: './edit-form-question-wrapper.html',
  styleUrl: './edit-form-question-wrapper.scss',
})
export class EditFormQuestionWrapper implements OnInit, AfterViewInit, OnDestroy {

  componentId = signal<string>(crypto.randomUUID())

  question = input.required<AnyQuestionRes>()
  orderIndex = input.required<number>()

  questionInput = viewChild<ElementRef<HTMLInputElement>>('questionInput')

  private questionHost = viewChild('questionHost', {read: ViewContainerRef})
  protected createdComponentRef?: ComponentRef<unknown>

  protected moreMenuItems = signal<ReadonlyArray<EditQuestionMoreMenuItem>>([])
  protected questionTypeGroups = signal(QUESTION_TYPE_GROUPS);
  protected selectedMoreMenuItemIds = signal<Set<string>>(new Set())

  protected canSaveQuestion = signal<boolean>(true)
  protected hasError = signal<boolean>(false)

  protected editFormStateService = inject(EditFormStateService)
  protected editFormService = inject(EditFormService)
  private componentFactory = inject(EditFormQuestionComponentFactory)

  protected formRes = this.editFormService.formRes

  protected formGroup = new FormGroup({
    question: new FormControl<string | null>(null),
    description: new FormControl<string | null>(null),
    questionType: new FormControl<QuestionType>(EditQuestionConstant.DEFAULT_SELECTED_QUESTION_TYPE, [Validators.required]),
    required: new FormControl<boolean>(false)
  })

  private selectedMoreMenuItemIdsEffect = effect(() => {
    const ids = this.selectedMoreMenuItemIds()
    this.createdComponentRef?.setInput('moreMenuItemIds', ids)
  })

  ngOnInit() {

    this.componentId.set(this.question().id)

    this.formGroup.patchValue({
      question: this.question().question,
      description: this.question().description,
      questionType: this.question().questionType,
      required: this.question().required
    })

    this.formGroup.valueChanges.subscribe(val => {
      if (this.formGroup.invalid) return

      const instance = this.createdComponentRef?.instance

      if (instance instanceof EditFormQuestionComponent) {

        const questionTypeChanged = val.questionType !== this.question().questionType
        let onlyQuestionAddUpdateReq: AnyOnlyQuestionAddUpdateReq

        if (questionTypeChanged) {
          onlyQuestionAddUpdateReq = DefaultQuestionAddUpdateReq.get(val.questionType!)
        } else {
          onlyQuestionAddUpdateReq = instance.getOnlyQuestionAddUpdateReq()
        }

        const questionAddUpdateReq: AnyQuestionAddUpdateReq = {
          ...onlyQuestionAddUpdateReq,
          question: val.question ?? null,
          description: val.description ?? null,
          questionType: val.questionType!,
          required: val.required ?? false
        }

        this.updateQuestion(questionAddUpdateReq, questionTypeChanged)
      }
    })

    this.createComponent(this.question().questionType, this.question())

    this.editFormStateService.changeFocus(this.componentId())
  }

  ngAfterViewInit() {
    queueMicrotask(() => {
      this.questionInput()?.nativeElement.focus()
    })
  }

  ngOnDestroy() {
    this.selectedMoreMenuItemIdsEffect.destroy()
    this.createdComponentRef?.destroy()
  }

  protected onCopyQuestionClick() {

    this.editFormStateService.isFormGettingModified.set(true)

    const questionAddUpdateReq: QuestionAddUpdateReq = {
      question: this.question().question,
      description: this.question().description,
      required: this.question().required,
      questionType: this.question().questionType
    }

    let onlyQuestionAddUpdateReq: AnyOnlyQuestionAddUpdateReq | null = null

    const instance = this.createdComponentRef?.instance
    if (instance instanceof EditFormQuestionComponent) {
      onlyQuestionAddUpdateReq = instance.getOnlyQuestionAddUpdateReq()
    }

    const question: AnyQuestionAddUpdateReq = {
      ...questionAddUpdateReq,
      ...onlyQuestionAddUpdateReq
    }

    this.editFormService.addQuestion(question, () => {
      this.editFormStateService.isFormGettingModified.set(false)
    })
  }

  protected onDeleteQuestionClick() {

    this.editFormStateService.isFormGettingModified.set(true)

    const deleteId = this.question().id
    const questions = this.formRes()?.questions ?? []

    const deleteQuestionIndex = questions.findIndex(q => q.id === deleteId)
    let nearestQuestionIndex = deleteQuestionIndex === questions.length - 1 ?
      deleteQuestionIndex - 1 : deleteQuestionIndex + 1

    this.editFormService.deleteQuestion(this.question(), () => {
      this.editFormStateService.isFormGettingModified.set(false)

      if (questions.length === 1) {
        this.editFormStateService.changeFocus("form-info")
      } else {
        const id = questions.at(nearestQuestionIndex)?.id
        if (id) {
          this.editFormStateService.changeFocus(id)
        }
      }
    })
  }

  protected toggleSelectedMoreMenuItemIds(itemId: string) {
    this.selectedMoreMenuItemIds.update(value => {
      const newValue = new Set(value)
      newValue.has(itemId) ? newValue.delete(itemId) : newValue.add(itemId)
      return newValue;
    })

    if (!this.selectedMoreMenuItemIds().has('description') && this.formGroup.value.description) {
      this.formGroup.controls.description.patchValue(null)
    }
  }

  protected addSelectedMoreMenuItemIds(itemId: string) {
    this.selectedMoreMenuItemIds.update(value => new Set([...value, itemId]))
  }

  protected showQuestionTypeSelector(): boolean {
    return this.editFormStateService.isFocused(this.componentId())
  }

  protected showDescription(): boolean {
    return this.selectedMoreMenuItemIds().has('description') && this.editFormStateService.isFocused(this.componentId())
  }

  protected showQuestionActions(): boolean {
    return this.editFormStateService.isFocused(this.componentId())
  }

  private createComponent(questionType: QuestionType, question: AnyQuestionRes) {
    this.removeComponent()

    this.componentFactory.getComponent(questionType).then(componentClass => {
      this.createdComponentRef = this.questionHost()?.createComponent(componentClass)

      this.createdComponentRef?.setInput('question', question)
      this.createdComponentRef?.setInput('parentComponentId', this.componentId())

      const instance = this.createdComponentRef?.instance
      if (instance instanceof EditFormQuestionComponent) {
        instance.moreMenuItemId.subscribe(val => this.toggleSelectedMoreMenuItemIds(val))
        instance.canSaveQuestion.subscribe(val => this.canSaveQuestion.set(val))
        instance.hasError.subscribe(val => this.hasError.set(val))
        instance.updateQuestion.subscribe(val => {
          const questionAddUpdateReq: AnyQuestionAddUpdateReq = {
            ...val,
            question: this.formGroup.value.question!,
            description: this.formGroup.value.description!,
            questionType: this.formGroup.value.questionType!,
            required: this.formGroup.value.required!,
          }
          this.updateQuestion(questionAddUpdateReq, false)
        })
      }

      this.selectedMoreMenuItemIds.set(new Set())
      this.setMoreMenuItems(questionType)

      if (question.description) {
        this.addSelectedMoreMenuItemIds('description')
      }
    })
  }

  private updateQuestion(questionAddUpdateReq: AnyQuestionAddUpdateReq, recreateComponent: boolean) {

    if (!this.canSaveQuestion()) return

    const showLoader = this.question().questionType !== questionAddUpdateReq.questionType

    if (showLoader) {
      this.editFormStateService.isFormGettingModified.set(true)
    }

    this.editFormService.updateQuestion(
      this.question().id, questionAddUpdateReq, questionRes => {

        if (showLoader) {
          this.editFormStateService.isFormGettingModified.set(false)
        }

        if (recreateComponent) {
          this.createComponent(questionRes.questionType, questionRes)
        }
      }
    )
  }

  private removeComponent() {
    if (this.createdComponentRef) {
      this.createdComponentRef.destroy()
      this.createdComponentRef = undefined
    }
  }

  private setMoreMenuItems(questionType: QuestionType) {
    const items = EditQuestionConstant.EDIT_QUESTION_MORE_MENU_ITEM_MAP.get(questionType);
    if (items) {
      this.moreMenuItems.set(items)
    }
  }
}
