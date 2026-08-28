import {Injectable, Type} from '@angular/core';
import {QuestionType} from '../type/question-type';

@Injectable({
  providedIn: 'root',
})
export class ViewFormQuestionComponentFactory {

  async getComponent(questionType: QuestionType): Promise<Type<any>> {
    switch (questionType) {
      case 'CHECKBOX':
        return (await import('../view-form/question/view-form-checkbox/view-form-checkbox')).ViewFormCheckbox;
      case 'DATE':
        return (await import('../view-form/question/view-form-date/view-form-date')).ViewFormDate;
      case 'DATE_TIME':
        return (await import('../view-form/question/view-form-date-time/view-form-date-time')).ViewFormDateTime;
      case 'DROPDOWN':
        return (await import('../view-form/question/view-form-dropdown/view-form-dropdown')).ViewFormDropdown;
      case 'DURATION':
        return (await import('../view-form/question/view-form-duration/view-form-duration')).ViewFormDuration;
      case 'FILE_UPLOAD':
        return (await import('../view-form/question/view-form-file-upload/view-form-file-upload')).ViewFormFileUpload;
      case 'LINEAR_SCALE':
        return (await import('../view-form/question/view-form-linear-scale/view-form-linear-scale')).ViewFormLinearScale;
      case 'MULTIPLE_CHOICE':
        return (await import('../view-form/question/view-form-multiple-choice/view-form-multiple-choice')).ViewFormMultipleChoice;
      case 'MULTIPLE_CHOICE_GRID':
        return (await import('../view-form/question/view-form-multiple-choice-grid/view-form-multiple-choice-grid')).ViewFormMultipleChoiceGrid;
      case 'PARAGRAPH':
        return (await import('../view-form/question/view-form-paragraph/view-form-paragraph')).ViewFormParagraph;
      case 'RATING':
        return (await import('../view-form/question/view-form-rating/view-form-rating')).ViewFormRating;
      case 'SHORT_ANSWER':
        return (await import('../view-form/question/view-form-short-answer/view-form-short-answer')).ViewFormShortAnswer;
      case 'TICK_BOX_GRID':
        return (await import('../view-form/question/view-form-tick-box-grid/view-form-tick-box-grid')).ViewFormTickBoxGrid;
      case 'TIME':
        return (await import('../view-form/question/view-form-time/view-form-time')).ViewFormTime;
    }
  }

}
