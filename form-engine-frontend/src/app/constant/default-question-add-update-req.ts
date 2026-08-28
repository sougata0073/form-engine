import {AnyQuestionRes} from '../type/any-question-res';
import {QuestionType} from '../type/question-type';
import {QuestionRes} from '../model/edit-form/question/response/question-res';
import {capitalize, random, startCase} from 'lodash';
import {AnyOnlyQuestionAddUpdateReq} from '../type/any-only-question-add-update-req';

export class DefaultQuestionAddUpdateReq {

  private static map = new Map<QuestionType, AnyOnlyQuestionAddUpdateReq>([
    ['CHECKBOX', {
      options: [{id: null, option: 'Option 1'}],
      validationConfig: {validationId: 'CHECKBOX_NONE', errorText: null}
    }],
    ['DATE', {}],
    ['DATE_TIME', {}],
    ['DROPDOWN', {options: [{id: null, option: 'Option 1'}]}],
    ['DURATION', {}],
    ['FILE_UPLOAD', {allowedFileCategories: [], maxFileSize: 10485760}],
    ['LINEAR_SCALE', {fromNumber: 1, toNumber: 5}],
    ['MULTIPLE_CHOICE', {options: [{id: null, option: 'Option 1'}]}],
    ['MULTIPLE_CHOICE_GRID', {
      eachRowRequired: false,
      rows: [{id: null, row: 'Row 1'}],
      columns: [{id: null, column: 'Column 1'}]
    }],
    ['PARAGRAPH', {validationConfig: {validationId: 'PARAGRAPH_NONE', errorText: null}}],
    ['RATING', {maxRatingNumber: 5, ratingIcon: 'STAR'}],
    ['SHORT_ANSWER', {validationConfig: {validationId: 'SHORT_ANSWER_NONE', errorText: null}}],
    ['TICK_BOX_GRID', {
      eachRowRequired: false,
      rows: [{id: null, row: 'Row 1'}],
      columns: [{id: null, column: 'Column 1'}]
    }],
    ['TIME', {}]
  ])

  static get(questionType: QuestionType): AnyOnlyQuestionAddUpdateReq {
    return structuredClone(this.map.get(questionType)!)
  }

}
