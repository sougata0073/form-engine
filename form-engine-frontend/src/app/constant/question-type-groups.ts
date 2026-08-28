import {QuestionType} from '../type/question-type';

export const QUESTION_TYPE_GROUPS = new Map<
  string,
  ReadonlyArray<{
    value: QuestionType;
    displayValue: string;
    icon: string;
  }>
>([
  [
    'text',
    [
      {value: 'SHORT_ANSWER', displayValue: 'Short Answer', icon: 'short_text'},
      {value: 'PARAGRAPH', displayValue: 'Paragraph', icon: 'subject'},
    ],
  ],

  [
    'multiOption',
    [
      {value: 'MULTIPLE_CHOICE', displayValue: 'Multiple Choice', icon: 'radio_button_checked'},
      {value: 'CHECKBOX', displayValue: 'Checkbox', icon: 'check_box'},
      {value: 'DROPDOWN', displayValue: 'Dropdown', icon: 'expand_circle_down'},
    ],
  ],

  [
    'upload',
    [
      {value: 'FILE_UPLOAD', displayValue: 'File Upload', icon: 'cloud_upload'},
    ],
  ],

  [
    'special',
    [
      {value: 'LINEAR_SCALE', displayValue: 'Linear Scale', icon: 'linear_scale'},
      {value: 'RATING', displayValue: 'Rating', icon: 'star'},
      {value: 'MULTIPLE_CHOICE_GRID', displayValue: 'Multiple Choice Grid', icon: 'apps'},
      {value: 'TICK_BOX_GRID', displayValue: 'Tick Box Grid', icon: 'grid_on'},
    ],
  ],

  [
    'dateTime',
    [
      {value: 'DATE', displayValue: 'Date', icon: 'today'},
      {value: 'TIME', displayValue: 'Time', icon: 'schedule'},
      {value: 'DATE_TIME', displayValue: 'Date Time', icon: 'calendar_month'},
      {value: 'DURATION', displayValue: 'Duration', icon: 'hourglass_bottom'},
    ],
  ],
]);
