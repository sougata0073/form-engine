import {Injectable} from '@angular/core';
import {ArrayUtil} from '../util/array-util';

@Injectable({
  providedIn: 'root',
})
export class ViewFormStateService {

  private formSubmitCallbacks: (() => void)[] = []
  private questionErrorMap = new Map<string, boolean>()

  addFormSubmitCallback(callbackFn: () => void) {
    this.formSubmitCallbacks.push(callbackFn)
  }

  executeAllFormSubmitCallbacks() {
    this.formSubmitCallbacks.forEach(cb => cb())
  }

  removeAllFormSubmitCallback() {
    this.formSubmitCallbacks.length = 0
  }

  putToQuestionErrorMap(questionId: string, hasError: boolean) {
    this.questionErrorMap.set(questionId, hasError)
  }

  ifAnyQuestionHasError() {
    const values = ArrayUtil.toArray(this.questionErrorMap.values())
    return values.some(val => val)
  }

}
