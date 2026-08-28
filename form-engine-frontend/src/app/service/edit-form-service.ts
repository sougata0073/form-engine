import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AnyQuestionRes } from '../type/any-question-res';
import { AnyQuestionAddUpdateReq } from '../type/any-question-add-update-req';
import { FormDetails } from '../model/form/form-details';
import { FormAddUpdateReq } from '../model/form/form-add-update-req';
import { SuccessMessage } from '../model/common/success-message';
import { debounce } from 'lodash';
import { FormInfoRes } from '../model/form/form-info-res';
import { moveItemInArray } from '@angular/cdk/drag-drop';
import { QuestionOrderUpdateReq } from '../model/edit-form/question/request/question-order-update-req';
import { QuestionRes } from '../model/edit-form/question/response/question-res';
import { CopyFormReq } from '../model/form/copy-form-req';

@Injectable({
  providedIn: 'root',
})
export class EditFormService {

  private http = inject(HttpClient)

  private _formInfo = signal<FormInfoRes | null>(null)
  formInfo = this._formInfo.asReadonly()

  private _formRes = signal<FormDetails | null>(null)
  formRes = this._formRes.asReadonly()

  updateFormInfo = debounce(
    (formId: string, form: FormAddUpdateReq, onComplete?: (res: FormInfoRes) => void) => {

      const url = `http://localhost:9092/api/v1/forms/${formId}`

      this.http.put<FormInfoRes>(url, form).subscribe(res => {
        this._formInfo.set(res)
        this._formRes.update(prev => {
          return { ...prev!, ...res }
        })

        onComplete?.(res)
      })
    }, 1000)


  loadFormInfo(formId: string, onComplete: (res: FormInfoRes) => void) {
    const url = `http://localhost:9092/api/v1/forms/${formId}/info`

    this.http.get<FormInfoRes>(url).subscribe(res => {
      this._formInfo.set(res)
      onComplete(res)
    })
  }

  loadFormRes(formId: string, onComplete?: (res: FormDetails) => void) {

    const url = `http://localhost:9092/api/v1/forms/${formId}`

    this.http.get<FormDetails>(url).subscribe(res => {

      this._formRes.set(res)

      onComplete?.(res)
    })
  }

  addQuestion(question: AnyQuestionAddUpdateReq, onComplete?: (res: AnyQuestionRes) => void) {

    const url = `http://localhost:9092/api/v1/forms/${this._formRes()!.id}/questions`

    this.http.post<AnyQuestionRes>(url, question).subscribe(res => {

      this._formRes.update(prev => {
        return { ...prev!, questions: [...prev!.questions, res] }
      })

      onComplete?.(res)
    })
  }

  updateQuestion = debounce(
    (
      questionId: string,
      question: AnyQuestionAddUpdateReq,
      onComplete?: (res: AnyQuestionRes) => void
    ) => {

      const url = `http://localhost:9092/api/v1/forms/${this._formRes()!.id}/questions/${questionId}`;

      this.http.put<AnyQuestionRes>(url, question).subscribe(res => {

        this._formRes.update(prev => {
          const newQuestions = prev!.questions.map(q =>
            q.id === res.id ? structuredClone(res) : structuredClone(q)
          );
          return { ...prev!, questions: newQuestions };
        });

        onComplete?.(res);
      });
    },
    1000
  );

  deleteQuestion(question: QuestionRes, onComplete?: () => void) {

    const url = `http://localhost:9092/api/v1/forms/${this._formRes()!.id}/questions/${question.id}`

    this.http.delete<SuccessMessage>(url)
      .subscribe(() => {
        this._formRes.update(prev => {

          const newQuestions = prev!.questions
            .filter(q => q.id !== question.id)
            .map(q => q.orderIndex > question.orderIndex ? { ...q, orderIndex: q.orderIndex - 1 } : q)

          return { ...prev!, questions: newQuestions }
        })

        onComplete?.()
      })
  }

  updateQuestionIndex(questionId: string, prevIndex: number, currIndex: number) {
    if (this._formRes()) {
      moveItemInArray(this._formRes()!.questions, prevIndex, currIndex)

      const url = `http://localhost:9092/api/v1/forms/${this.formRes()!.id}/questions/${questionId}/order`
      const reqBody: QuestionOrderUpdateReq = {
        currentIndex: currIndex
      }

      this.http.patch<SuccessMessage>(url, reqBody).subscribe({
        next: res => {

        },
        complete: () => {

        },
        error: (err: HttpErrorResponse) => {
          moveItemInArray(this._formRes()!.questions, currIndex, prevIndex)
        }
      })
    }
  }

  close() {
  }

}
