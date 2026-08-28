import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AnyQuestionRes} from '../type/any-question-res';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {

  private http = inject(HttpClient)

  getQuestionDetails(formId: string, questionId: string, onComplete: (res: AnyQuestionRes) => void) {
    const url = `http://localhost:9092/api/v1/forms/${formId}/questions/${questionId}`;

    this.http.get<AnyQuestionRes>(url).subscribe(res => {
      onComplete(res)
    })
  }

}
