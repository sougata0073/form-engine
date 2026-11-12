import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AnyQuestionRes } from '../type/any-question-res';
import { AnyQuestionAddReq } from '../type/any-question-add-req';
import { FormDetails } from '../model/form/form-details';
import { SuccessMessage } from '../model/common/success-message';
import { mergeWith } from 'lodash';
import { FormInfo } from '../model/form/form-info';
import { moveItemInArray } from '@angular/cdk/drag-drop';
import { QuestionOrderUpdateReq } from '../model/edit-form/question/updatereq/question-order-update-req';
import { QuestionRes } from '../model/edit-form/question/response/question-res';
import { AnyQuestionUpdateReq } from '../type/any-question-update-req';
import { MultipleQuestionUpdateReq } from '../model/edit-form/question/updatereq/multiple-question-update-req';
import { MultipleQuestionDetailsRes } from '../model/edit-form/question/response/multiple-question-details-res';
import { TaskWeight } from '../type/task-weight';
import { FileUploadUpdateReq } from '../model/edit-form/question/updatereq/file-upload-update-req';
import { CheckboxUpdateReq } from '../model/edit-form/question/updatereq/checkbox-update-req';
import { DropdownUpdateReq } from '../model/edit-form/question/updatereq/dropdown-update-req';
import { MultipleChoiceUpdateReq } from '../model/edit-form/question/updatereq/multiple-choice-update-req';
import { MultipleChoiceGridUpdateReq } from '../model/edit-form/question/updatereq/multiple-choice-grid-update-req';
import { TickBoxGridUpdateReq } from '../model/edit-form/question/updatereq/tick-box-grid-update-req';
import { FormInfoUpdateReq } from '../model/form/form-info-update-req';
import { FormDefaultSettings } from '../model/form/form-default-settings';
import { FormDefaultSettingsUpdateReq } from '../model/form/form-default-settings-update-req';

@Injectable({
  providedIn: 'root',
})
export class EditFormService {
  private http = inject(HttpClient);

  private _formInfo = signal<FormInfo | null>(null);
  formInfo = this._formInfo.asReadonly();

  private _formRes = signal<FormDetails | null>(null);
  formRes = this._formRes.asReadonly();

  private updateQuestionRequestMap: Map<
    string,
    { req: AnyQuestionUpdateReq; onComplete?: (res: AnyQuestionRes) => void }
  > = new Map();
  private updateQuestionTimer?: ReturnType<typeof setTimeout>;
  private updateQuestionDebounceWait: number = 1000;

  private updateFormInfoRequestStore: {
    req: FormInfoUpdateReq;
    onCompleteCallbacks: (((res: FormInfo) => void) | undefined)[];
  } | null = null;
  private updateFormInfoTimer?: ReturnType<typeof setTimeout>;
  private updateFormInfoDebounceWait: number = 1000;

  private updateFormDefaultSettingsRequestStore: {
    req: FormDefaultSettingsUpdateReq;
    onCompleteCallbacks: (((res: FormDefaultSettings) => void) | undefined)[];
  } | null = null;
  private updateFormDefaultSettingsTimer?: ReturnType<typeof setTimeout>;
  private updateFormDefaultSettingsDebounceWait: number = 1000;

  updateFormInfo(
    formId: string,
    formInfoUpdateReq: FormInfoUpdateReq,
    onComplete?: (res: FormInfo) => void,
  ) {
    if (this.updateFormInfoRequestStore !== null) {
      const prevReq = this.updateFormInfoRequestStore.req;

      const newReq = mergeWith({}, prevReq, formInfoUpdateReq, (finalValue, srcValue, key) => {
        if (finalValue && srcValue && key === ('updateFields' satisfies keyof FormInfoUpdateReq)) {
          return [...new Set([...finalValue, ...srcValue])];
        }

        return undefined;
      });

      const newOnCompleteCallbacks = this.updateFormInfoRequestStore.onCompleteCallbacks;
      newOnCompleteCallbacks.push(onComplete);

      this.updateFormInfoRequestStore = {
        req: newReq,
        onCompleteCallbacks: newOnCompleteCallbacks,
      };
    } else {
      this.updateFormInfoRequestStore = {
        req: formInfoUpdateReq,
        onCompleteCallbacks: [onComplete],
      };
    }

    const updateReqFun: () => void = () => {
      const onCompleteCallbacksSnapshot = this.updateFormInfoRequestStore?.onCompleteCallbacks;
      const url = `forms/${formId}`;

      this.http.patch<FormInfo>(url, this.updateFormInfoRequestStore?.req).subscribe((res) => {
        this._formInfo.set(res);
        this._formRes.update((prev) => {
          if (prev) {
            return { ...prev, formInfo: res };
          }

          return null;
        });

        onCompleteCallbacksSnapshot?.forEach((callBack) => callBack?.(res));
      });

      this.updateFormInfoRequestStore = null;
    };

    if (this.updateFormInfoTimer) {
      clearTimeout(this.updateFormInfoTimer);
    }

    this.updateFormInfoTimer = setTimeout(updateReqFun, this.updateFormInfoDebounceWait);
  }

  updateQuestion(
    questionUpdateReq: AnyQuestionUpdateReq,
    requestWeight: TaskWeight,
    onComplete?: (res: AnyQuestionRes) => void,
  ) {
    if (this.updateQuestionRequestMap.has(questionUpdateReq.questionId)) {
      const prevReq = this.updateQuestionRequestMap.get(questionUpdateReq.questionId)!.req;
      const newReq = mergeWith({}, prevReq, questionUpdateReq, (finalValue, srcValue, key) => {
        if (
          finalValue &&
          srcValue &&
          (key === ('updateFields' satisfies keyof AnyQuestionUpdateReq) ||
            key === ('allowedFileCategories' satisfies keyof FileUploadUpdateReq))
        ) {
          return [...new Set([...finalValue, ...srcValue])];
        }

        if (
          finalValue &&
          srcValue &&
          (key === ('options' satisfies keyof CheckboxUpdateReq) ||
            key === ('options' satisfies keyof DropdownUpdateReq) ||
            key === ('options' satisfies keyof MultipleChoiceUpdateReq) ||
            key === ('rows' satisfies keyof MultipleChoiceGridUpdateReq) ||
            key === ('rows' satisfies keyof TickBoxGridUpdateReq) ||
            key === ('columns' satisfies keyof MultipleChoiceGridUpdateReq) ||
            key === ('columns' satisfies keyof TickBoxGridUpdateReq))
        ) {
          const prevMap = new Map(finalValue.map((x: any) => [x.id, x]));
          const currMap = new Map(srcValue.map((x: any) => [x.id, x]));

          const newMap = new Map([...prevMap, ...currMap]);

          return [...newMap.values()];
        }

        return undefined;
      });

      this.updateQuestionRequestMap.set(questionUpdateReq.questionId, {
        req: newReq,
        onComplete: onComplete,
      });
    } else {
      this.updateQuestionRequestMap.set(questionUpdateReq.questionId, {
        req: questionUpdateReq,
        onComplete: onComplete,
      });
    }

    const updateReqFun: () => void = () => {
      const reqMapSnapshot = new Map(this.updateQuestionRequestMap);

      const url = `forms/${this._formRes()!.formInfo.id}/questions`;

      const req: MultipleQuestionUpdateReq = {
        questions: [...reqMapSnapshot.values()].map((v) => v.req),
      };

      this.http.patch<MultipleQuestionDetailsRes>(url, req).subscribe((res) => {
        const updatedQuestionMap = new Map<string, AnyQuestionRes>(
          res.questions.map((q) => [q.id, q]),
        );

        this._formRes.update((prev) => {
          const newQuestions = prev!.questions.map((q) => {
            const updatedQuestion = updatedQuestionMap.get(q.id);

            if (updatedQuestion) {
              return updatedQuestion;
            }

            return q;
          });
          return { ...prev!, questions: newQuestions };
        });

        res.questions.forEach((q) => {
          reqMapSnapshot.get(q.id)?.onComplete?.(q);
        });
      });

      this.updateQuestionRequestMap.clear();
    };

    if (this.updateQuestionTimer) {
      clearTimeout(this.updateQuestionTimer);
    }

    if (requestWeight === 'CRITICAL') {
      updateReqFun();
    } else {
      this.updateQuestionTimer = setTimeout(updateReqFun, this.updateQuestionDebounceWait);
    }
  }

  loadFormInfo(formId: string, onComplete: (res: FormInfo) => void) {
    const url = `forms/${formId}/info`;

    this.http.get<FormInfo>(url).subscribe((res) => {
      this._formInfo.set(res);
      onComplete(res);
    });
  }

  loadFormRes(formId: string, onComplete?: (res: FormDetails) => void) {
    const url = `forms/${formId}`;

    this.http.get<FormDetails>(url).subscribe((res) => {
      this._formRes.set(res);

      onComplete?.(res);
    });
  }

  addQuestion(question: AnyQuestionAddReq, onComplete?: (res: AnyQuestionRes) => void) {
    const url = `forms/${this._formRes()!.formInfo.id}/questions`;

    this.http.post<AnyQuestionRes>(url, question).subscribe((res) => {
      this._formRes.update((prev) => {
        if (!prev) return prev;

        return { ...prev, questions: [...prev.questions, res] };
      });

      onComplete?.(res);
    });
  }

  copyQuestion(questionId: string, onComplete?: (res: AnyQuestionRes) => void) {
    const url = `forms/${this._formRes()!.formInfo.id}/questions/${questionId}/copy`;

    this.http.post<AnyQuestionRes>(url, {}).subscribe((res) => {
      this._formRes.update((prev) => {
        if (!prev) return prev;

        const parentQuestion = prev.questions.find((q) => q.id === questionId);

        prev.questions.forEach((q) => {
          if (q.orderIndex > (parentQuestion?.orderIndex ?? Number.MAX_VALUE)) {
            q.orderIndex = q.orderIndex + 1;
          }
        });

        prev.questions.push(res);

        prev.questions.sort((a, b) => a.orderIndex - b.orderIndex);

        return prev;
      });

      onComplete?.(res);
    });
  }

  deleteQuestion(question: QuestionRes, onComplete?: () => void) {
    const url = `forms/${this._formRes()!.formInfo.id}/questions/${question.id}`;

    this.http.delete<SuccessMessage>(url).subscribe(() => {
      this._formRes.update((prev) => {
        const newQuestions = prev!.questions
          .filter((q) => q.id !== question.id)
          .map((q) =>
            q.orderIndex > question.orderIndex ? { ...q, orderIndex: q.orderIndex - 1 } : q,
          );

        return { ...prev!, questions: newQuestions };
      });

      onComplete?.();
    });
  }

  updateQuestionIndex(
    questionId: string,
    prevIndex: number,
    currIndex: number,
    onComplete?: (res: SuccessMessage) => void,
  ) {
    if (this._formRes()) {
      moveItemInArray(this._formRes()!.questions, prevIndex, currIndex);

      const url = `forms/${this.formRes()!.formInfo.id}/questions/${questionId}/order`;
      const reqBody: QuestionOrderUpdateReq = {
        currentIndex: currIndex,
      };

      this.http.patch<SuccessMessage>(url, reqBody).subscribe({
        next: (res) => {
          onComplete?.(res);
        },
        complete: () => {},
        error: (err: HttpErrorResponse) => {
          moveItemInArray(this._formRes()!.questions, currIndex, prevIndex);
        },
      });
    }
  }

  getFormDefaultSettings(onComplete: (res: FormDefaultSettings) => void) {
    const url = 'forms/default-settings';

    this.http.get<FormDefaultSettings>(url).subscribe((res) => {
      onComplete(res);
    });
  }

  updateFormDefaultSettings(
    formDefaultSettingsUpdateReq: FormDefaultSettingsUpdateReq,
    onComplete?: (res: FormDefaultSettings) => void,
  ) {
    if (this.updateFormDefaultSettingsRequestStore !== null) {
      const prevReq = this.updateFormDefaultSettingsRequestStore.req;

      const newReq = mergeWith(
        {},
        prevReq,
        formDefaultSettingsUpdateReq,
        (finalValue, srcValue, key) => {
          if (
            finalValue &&
            srcValue &&
            key === ('updateFields' satisfies keyof FormDefaultSettingsUpdateReq)
          ) {
            return [...new Set([...finalValue, ...srcValue])];
          }

          return undefined;
        },
      );

      const newOnCompleteCallbacks = this.updateFormDefaultSettingsRequestStore.onCompleteCallbacks;
      newOnCompleteCallbacks.push(onComplete);

      this.updateFormDefaultSettingsRequestStore = {
        req: newReq,
        onCompleteCallbacks: newOnCompleteCallbacks,
      };
    } else {
      this.updateFormDefaultSettingsRequestStore = {
        req: formDefaultSettingsUpdateReq,
        onCompleteCallbacks: [onComplete],
      };
    }

    const updateReqFun: () => void = () => {
      const onCompleteCallbacksSnapshot =
        this.updateFormDefaultSettingsRequestStore?.onCompleteCallbacks;
      const url = `forms/default-settings`;

      this.http
        .patch<FormDefaultSettings>(url, this.updateFormDefaultSettingsRequestStore?.req)
        .subscribe((res) => {
          onCompleteCallbacksSnapshot?.forEach((callBack) => callBack?.(res));
        });

      this.updateFormDefaultSettingsRequestStore = null;
    };

    if (this.updateFormDefaultSettingsTimer) {
      clearTimeout(this.updateFormDefaultSettingsTimer);
    }

    this.updateFormDefaultSettingsTimer = setTimeout(
      updateReqFun,
      this.updateFormDefaultSettingsDebounceWait,
    );
  }
}
