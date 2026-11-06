import { Injectable } from '@angular/core';
import { ArrayUtil } from '../util/array-util';

@Injectable({
  providedIn: 'root',
})
export class ViewFormStateService {
  private formSubmitCallbacks: Map<string, () => void> = new Map();
  private questionErrorMap = new Map<string, boolean>();

  setFormSubmitCallback(questionId: string, callbackFn: () => void) {
    this.formSubmitCallbacks.set(questionId, callbackFn);
  }

  executeAllFormSubmitCallbacks() {
    this.formSubmitCallbacks.forEach((callback, questionId) => callback());
  }

  removeAllFormSubmitCallback() {
    this.formSubmitCallbacks.clear();
  }

  putToQuestionErrorMap(questionId: string, hasError: boolean) {
    this.questionErrorMap.set(questionId, hasError);
  }

  ifAnyQuestionHasError() {
    const values = ArrayUtil.toArray(this.questionErrorMap.values());
    return values.some((val) => val);
  }
}
