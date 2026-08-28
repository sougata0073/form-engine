import {Injectable, Type} from '@angular/core';
import {QuestionType} from '../type/question-type';

@Injectable({
  providedIn: 'root'
})
export class FormResponseQuestionComponentFactory {

  async getComponent(questionType: QuestionType): Promise<Type<any>> {
    switch (questionType) {
      case "CHECKBOX":
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-checkbox/edit-form-response-question-checkbox')).EditFormResponseQuestionCheckbox;
      case 'DATE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-date/edit-form-response-question-date')).EditFormResponseQuestionDate;
      case 'DATE_TIME':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-date-time/edit-form-response-question-date-time')).EditFormResponseQuestionDateTime;
      case 'DROPDOWN':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-dropdown/edit-form-response-question-dropdown')).EditFormResponseQuestionDropdown;
      case 'DURATION':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-duration/edit-form-response-question-duration')).EditFormResponseQuestionDuration;
      case 'FILE_UPLOAD':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-file-upload/edit-form-response-question-file-upload')).EditFormResponseQuestionFileUpload;
      case 'LINEAR_SCALE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-linear-scale/edit-form-response-question-linear-scale')).EditFormResponseQuestionLinearScale;
      case 'MULTIPLE_CHOICE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-multiple-choice/edit-form-response-question-multiple-choice')).EditFormResponseQuestionMultipleChoice;
      case 'MULTIPLE_CHOICE_GRID':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-multiple-choice-grid/edit-form-response-question-multiple-choice-grid')).EditFormResponseQuestionMultipleChoiceGrid;
      case 'PARAGRAPH':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-paragraph/edit-form-response-question-paragraph')).EditFormResponseQuestionParagraph;
      case 'RATING':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-rating/edit-form-response-question-rating')).EditFormResponseQuestionRating;
      case 'SHORT_ANSWER':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-short-answer/edit-form-response-question-short-answer')).EditFormResponseQuestionShortAnswer;
      case 'TICK_BOX_GRID':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-tick-box-grid/edit-form-response-question-tick-box-grid')).EditFormResponseQuestionTickBoxGrid;
      case 'TIME':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-question/response/edit-form-response-question-time/edit-form-response-question-time')).EditFormResponseQuestionTime;
    }
  }

}
