import {ValidationIdMeta} from '../type/validation-id-meta';
import {QuestionConstant} from './question-constant';

export type ShortAnswerActiveValidationInputId = 'NUMBER' | 'FROM_TO_NUMBER' | 'PATTERN' | null

export class ShortAnswerConstant extends QuestionConstant<ShortAnswerActiveValidationInputId> {

  override VALIDATION_ID_METAS: ReadonlyArray<ValidationIdMeta<ShortAnswerActiveValidationInputId>> = [
    {
      validationId: 'SHORT_ANSWER_NUMBER_GREATER_THAN',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'GREATER_THAN',
        label: 'Greater than',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_GREATER_THAN_OR_EQUAL_TO',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'GREATER_THAN_OR_EQUAL_TO',
        label: 'Greater than or equal to',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_LESS_THAN',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'LESS_THAN',
        label: 'Less than',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_LESS_THAN_OR_EQUAL_TO',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'LESS_THAN_OR_EQUAL_TO',
        label: 'Less than or equal to',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_EQUAL_TO',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'EQUAL_TO',
        label: 'Equal to',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_NOT_EQUAL_TO',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'NOT_EQUAL_TO',
        label: 'Not equal to',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_BETWEEN',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'BETWEEN',
        label: 'Between',
      },
      activeValidationInputId: 'FROM_TO_NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_NOT_BETWEEN',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'NOT_BETWEEN',
        label: 'Not between',
      },
      activeValidationInputId: 'FROM_TO_NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_IS_NUMBER',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'IS_NUMBER',
        label: 'Is number',
      },
      activeValidationInputId: null
    },
    {
      validationId: 'SHORT_ANSWER_NUMBER_WHOLE_NUMBER',
      input: {
        value: 'NUMBER',
        label: 'Number',
      },
      validation: {
        value: 'WHOLE_NUMBER',
        label: 'Whole number',
      },
      activeValidationInputId: null
    },
    {
      validationId: 'SHORT_ANSWER_TEXT_CONTAINS',
      input: {
        value: 'TEXT',
        label: 'Text',
      },
      validation: {
        value: 'CONTAINS',
        label: 'Contains',
      },
      activeValidationInputId: 'PATTERN'
    },
    {
      validationId: 'SHORT_ANSWER_TEXT_NOT_CONTAINS',
      input: {
        value: 'TEXT',
        label: 'Text',
      },
      validation: {
        value: 'NOT_CONTAINS',
        label: 'Not contains',
      },
      activeValidationInputId: 'PATTERN'
    },
    {
      validationId: 'SHORT_ANSWER_TEXT_EMAIL',
      input: {
        value: 'TEXT',
        label: 'Text',
      },
      validation: {
        value: 'EMAIL',
        label: 'Email',
      },
      activeValidationInputId: null
    },
    {
      validationId: 'SHORT_ANSWER_TEXT_URL',
      input: {
        value: 'TEXT',
        label: 'Text',
      },
      validation: {
        value: 'URL',
        label: 'URL',
      },
      activeValidationInputId: null
    },
    {
      validationId: 'SHORT_ANSWER_LENGTH_MAX_CHARACTER_COUNT',
      input: {
        value: 'LENGTH',
        label: 'Length',
      },
      validation: {
        value: 'MAX_CHARACTER_COUNT',
        label: 'Maximum character count',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_LENGTH_MIN_CHARACTER_COUNT',
      input: {
        value: 'LENGTH',
        label: 'Length',
      },
      validation: {
        value: 'MIN_CHARACTER_COUNT',
        label: 'Minimum character count',
      },
      activeValidationInputId: 'NUMBER'
    },
    {
      validationId: 'SHORT_ANSWER_REGEX_MATCHES',
      input: {
        value: 'REGEX',
        label: 'Regular expression',
      },
      validation: {
        value: 'MATCHES',
        label: 'Matches',
      },
      activeValidationInputId: 'PATTERN'
    },
    {
      validationId: 'SHORT_ANSWER_REGEX_DOES_NOT_MATCHES',
      input: {
        value: 'REGEX',
        label: 'Regular expression',
      },
      validation: {
        value: 'DOES_NOT_MATCHES',
        label: 'Does not matches',
      },
      activeValidationInputId: 'PATTERN'
    }
  ]

}
