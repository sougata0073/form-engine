import { QuestionRes } from '../model/edit-form/question/response/question-res';
import { Directive, input, OnChanges, output, SimpleChange, SimpleChanges } from '@angular/core';
import { AnyOnlyQuestionAddUpdateReq } from './any-only-question-add-update-req';
import { AnyOnlyQuestionUpdateReq } from './any-onlyquestion-update-req';
import { OnlyQuestionUpdateReq } from '../model/edit-form/question/updatereq/only-question-update-req';
import { TaskWeight } from './task-weight';

@Directive()
export abstract class EditFormQuestionComponent<
  Q extends QuestionRes,
  OQUR extends OnlyQuestionUpdateReq,
> implements OnChanges {
  question = input.required<Q>();
  parentComponentId = input.required<string>();
  moreMenuItemIds = input<Set<string>>(new Set());

  moreMenuItemId = output<string>();
  canSaveQuestion = output<boolean>();
  hasError = output<boolean>();
  updateQuestion = output<OQUR | { req: OQUR; updateType: TaskWeight }>();

  ngOnChanges(changes: SimpleChanges): void {
    const questionChange = changes['question'];

    if (questionChange) {
      this.onQuestionInputChange(questionChange);
    }
  }

  abstract onQuestionInputChange(change: SimpleChange<Q>): void;
}
