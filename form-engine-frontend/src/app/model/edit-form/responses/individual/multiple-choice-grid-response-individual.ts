import {ResponseIndividual} from './response-individual';

export interface MultipleChoiceGridResponseIndividual extends ResponseIndividual {
  rows: MultipleChoiceGridResponseIndividualRow[]
}

export interface MultipleChoiceGridResponseIndividualRow {
  rowId: string,
  columnId: string
}
