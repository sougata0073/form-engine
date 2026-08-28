import {Injectable, Type} from '@angular/core';
import {QuestionType} from '../type/question-type';

@Injectable({
  providedIn: 'root'
})
export class FormResponseSummaryComponentFactory {

  async getComponent(questionType: QuestionType): Promise<Type<any>> {
    switch (questionType) {
      case "CHECKBOX":
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-checkbox/edit-form-response-summary-checkbox')).EditFormResponseSummaryCheckbox;
      case 'DATE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-date/edit-form-response-summary-date')).EditFormResponseSummaryDate;
      case 'DATE_TIME':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-date-time/edit-form-response-summary-date-time')).EditFormResponseSummaryDateTime;
      case 'DROPDOWN':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-dropdown/edit-form-response-summary-dropdown')).EditFormResponseSummaryDropdown;
      case 'DURATION':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-duration/edit-form-response-summary-duration')).EditFormResponseSummaryDuration;
      case 'FILE_UPLOAD':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-file-upload/edit-form-response-summary-file-upload')).EditFormResponseSummaryFileUpload;
      case 'LINEAR_SCALE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-linear-scale/edit-form-response-summary-linear-scale')).EditFormResponseSummaryLinearScale;
      case 'MULTIPLE_CHOICE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-multiple-choice/edit-form-response-summary-multiple-choice')).EditFormResponseSummaryMultipleChoice;
      case 'MULTIPLE_CHOICE_GRID':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-multiple-choice-grid/edit-form-response-summary-multiple-choice-grid')).EditFormResponseSummaryMultipleChoiceGrid;
      case 'PARAGRAPH':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-paragraph/edit-form-response-summary-paragraph')).EditFormResponseSummaryParagraph;
      case 'RATING':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-rating/edit-form-response-summary-rating')).EditFormResponseSummaryRating;
      case 'SHORT_ANSWER':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-short-answer/edit-form-response-summary-short-answer')).EditFormResponseSummaryShortAnswer;
      case 'TICK_BOX_GRID':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-tick-box-grid/edit-form-response-summary-tick-box-grid')).EditFormResponseSummaryTickBoxGrid;
      case 'TIME':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-summary/response/edit-form-response-summary-time/edit-form-response-summary-time')).EditFormResponseSummaryTime;
    }
  }

}
