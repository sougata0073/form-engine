import {QuestionType} from '../type/question-type';
import {EditQuestionMoreMenuItem} from '../type/edit-question-more-menu-item';

export class EditQuestionConstant {

  static DEFAULT_SELECTED_QUESTION_TYPE: QuestionType = 'SHORT_ANSWER'

  static EDIT_QUESTION_MORE_MENU_ITEM_MAP = new Map<QuestionType, ReadonlyArray<EditQuestionMoreMenuItem>>([
    [
      'SHORT_ANSWER', [
      {itemId: 'description', label: 'Description'},
      {itemId: 'responseValidation', label: 'Response validation'}
    ]
    ],
    [
      'PARAGRAPH', [
      {itemId: 'description', label: 'Description'},
      {itemId: 'responseValidation', label: 'Response validation'}
    ]
    ],
    [
      'MULTIPLE_CHOICE', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'CHECKBOX', [
      {itemId: 'description', label: 'Description'},
      {itemId: 'responseValidation', label: 'Response validation'}
    ]
    ],
    [
      'DROPDOWN', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'FILE_UPLOAD', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'LINEAR_SCALE', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'RATING', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'MULTIPLE_CHOICE_GRID', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'TICK_BOX_GRID', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'DATE', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'TIME', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'DATE_TIME', [
      {itemId: 'description', label: 'Description'}
    ]
    ],
    [
      'DURATION', [
      {itemId: 'description', label: 'Description'}
    ]
    ]
  ])
}
