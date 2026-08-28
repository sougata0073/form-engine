import {ValidationIdMeta} from '../type/validation-id-meta';
import {QuestionConstant} from './question-constant';

export type ParagraphActiveValidationInputId = 'NUMBER' | 'PATTERN' | null

export class ParagraphConstant extends QuestionConstant<ParagraphActiveValidationInputId> {

  override VALIDATION_ID_METAS: ReadonlyArray<ValidationIdMeta<ParagraphActiveValidationInputId>> = [
    {
      validationId: 'PARAGRAPH_LENGTH_MAX_CHARACTER_COUNT',
      input: {value: 'LENGTH', label: 'Length'},
      validation: {value: 'MAX_CHARACTER_COUNT', label: 'Max character count'},
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'PARAGRAPH_LENGTH_MIN_CHARACTER_COUNT',
      input: {value: 'LENGTH', label: 'Length'},
      validation: {value: 'MIN_CHARACTER_COUNT', label: 'Min character count'},
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'PARAGRAPH_REGEX_MATCHES',
      input: {value: 'REGEX', label: 'Regular expression'},
      validation: {value: 'MATCHES', label: 'Matches'},
      activeValidationInputId: 'PATTERN'
    },
    {
      validationId: 'PARAGRAPH_REGEX_DOES_NOT_MATCHES',
      input: {value: 'REGEX', label: 'Regular expression'},
      validation: {value: 'DOES_NOT_MATCHES', label: 'Does not matches'},
      activeValidationInputId: 'PATTERN'
    }
  ]
}
