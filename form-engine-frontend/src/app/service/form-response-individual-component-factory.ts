import {Injectable, Type} from '@angular/core';
import {QuestionType} from '../type/question-type';

@Injectable({
  providedIn: 'root'
})
export class FormResponseIndividualComponentFactory {

  async getComponent(questionType: QuestionType): Promise<Type<any>> {
    switch (questionType) {
      case "CHECKBOX":
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-checkbox/edit-form-response-individual-checkbox')).EditFormResponseIndividualCheckbox;
      case 'DATE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-date/edit-form-response-individual-date')).EditFormResponseIndividualDate;
      case 'DATE_TIME':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-date-time/edit-form-response-individual-date-time')).EditFormResponseIndividualDateTime;
      case 'DROPDOWN':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-dropdown/edit-form-response-individual-dropdown')).EditFormResponseIndividualDropdown;
      case 'DURATION':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-duration/edit-form-response-individual-duration')).EditFormResponseIndividualDuration;
      case 'FILE_UPLOAD':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-file-upload/edit-form-response-individual-file-upload')).EditFormResponseIndividualFileUpload;
      case 'LINEAR_SCALE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-linear-scale/edit-form-response-individual-linear-scale')).EditFormResponseIndividualLinearScale;
      case 'MULTIPLE_CHOICE':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-multiple-choice/edit-form-response-individual-multiple-choice')).EditFormResponseIndividualMultipleChoice;
      case 'MULTIPLE_CHOICE_GRID':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-multiple-choice-grid/edit-form-response-individual-multiple-choice-grid')).EditFormResponseIndividualMultipleChoiceGrid;
      case 'PARAGRAPH':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-paragraph/edit-form-response-individual-paragraph')).EditFormResponseIndividualParagraph;
      case 'RATING':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-rating/edit-form-response-individual-rating')).EditFormResponseIndividualRating;
      case 'SHORT_ANSWER':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-short-answer/edit-form-response-individual-short-answer')).EditFormResponseIndividualShortAnswer;
      case 'TICK_BOX_GRID':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-tick-box-grid/edit-form-response-individual-tick-box-grid')).EditFormResponseIndividualTickBoxGrid;
      case 'TIME':
        return (await import('../edit-form/edit-form-responses/edit-form-responses-individual/response/edit-form-response-individual-time/edit-form-response-individual-time')).EditFormResponseIndividualTime;
    }
  }

}
