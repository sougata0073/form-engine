import {Injectable, Type} from '@angular/core';
import {QuestionType} from '../type/question-type';

@Injectable({
  providedIn: 'root'
})
export class EditFormQuestionComponentFactory {

  async getComponent(questionType: QuestionType): Promise<Type<any>> {
    switch (questionType) {
      case 'CHECKBOX':
        return (await import('../edit-form/edit-form-questions/question/edit-form-checkbox/edit-form-checkbox')).EditFormCheckbox;
      case 'DATE':
        return (await import('../edit-form/edit-form-questions/question/edit-form-date/edit-form-date')).EditFormDate;
      case 'DATE_TIME':
        return (await import('../edit-form/edit-form-questions/question/edit-form-date-time/edit-form-date-time')).EditFormDateTime;
      case 'DROPDOWN':
        return (await import('../edit-form/edit-form-questions/question/edit-form-dropdown/edit-form-dropdown')).EditFormDropdown;
      case 'DURATION':
        return (await import('../edit-form/edit-form-questions/question/edit-form-duration/edit-form-duration')).EditFormDuration;
      case 'FILE_UPLOAD':
        return (await import('../edit-form/edit-form-questions/question/edit-form-file-upload/edit-form-file-upload')).EditFormFileUpload;
      case 'LINEAR_SCALE':
        return (await import('../edit-form/edit-form-questions/question/edit-form-linear-scale/edit-form-linear-scale')).EditFormLinearScale;
      case 'MULTIPLE_CHOICE':
        return (await import('../edit-form/edit-form-questions/question/edit-form-multiple-choice/edit-form-multiple-choice')).EditFormMultipleChoice;
      case 'MULTIPLE_CHOICE_GRID':
        return (await import('../edit-form/edit-form-questions/question/edit-form-multiple-choice-grid/edit-form-multiple-choice-grid')).EditFormMultipleChoiceGrid;
      case 'PARAGRAPH':
        return (await import('../edit-form/edit-form-questions/question/edit-form-paragraph/edit-form-paragraph')).EditFormParagraph;
      case 'RATING':
        return (await import('../edit-form/edit-form-questions/question/edit-form-rating/edit-form-rating')).EditFormRating;
      case 'SHORT_ANSWER':
        return (await import('../edit-form/edit-form-questions/question/edit-form-short-answer/edit-form-short-answer')).EditFormShortAnswer;
      case 'TICK_BOX_GRID':
        return (await import('../edit-form/edit-form-questions/question/edit-form-tick-box-grid/edit-form-tick-box-grid')).EditFormTickBoxGrid;
      case 'TIME':
        return (await import('../edit-form/edit-form-questions/question/edit-form-time/edit-form-time')).EditFormTime;
    }
  }
}

