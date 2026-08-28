import {ValidationIdMeta} from '../type/validation-id-meta';
import {QuestionConstant} from './question-constant';

export type CheckboxActiveValidationInputId = 'NUMBER' | null

export class CheckboxConstant extends QuestionConstant<CheckboxActiveValidationInputId> {

  override VALIDATION_ID_METAS: ReadonlyArray<ValidationIdMeta<CheckboxActiveValidationInputId>> = [
    {
      validationId: 'CHECKBOX_SELECT_AT_LEAST',
      input: {value: 'SELECT_AT_LEAST', label: 'Select at least'},
      validation: {value: 'SELECT_AT_LEAST', label: 'Select at least'},
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'CHECKBOX_SELECT_AT_MOST',
      input: {value: 'SELECT_AT_MOST', label: 'Select at most'},
      validation: {value: 'SELECT_AT_MOST', label: 'Select at most'},
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'CHECKBOX_SELECT_EXACTLY',
      input: {value: 'SELECT_EXACTLY', label: 'Select exactly'},
      validation: {value: 'SELECT_EXACTLY', label: 'Select exactly'},
      activeValidationInputId: 'NUMBER'
    }
  ]

}
