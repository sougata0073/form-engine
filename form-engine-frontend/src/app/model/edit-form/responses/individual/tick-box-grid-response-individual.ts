import {ResponseIndividual} from './response-individual';

export interface TickBoxGridResponseIndividual extends ResponseIndividual {
  rows: TickBoxGridResponseIndividualRow[]
}

export interface TickBoxGridResponseIndividualRow {
  rowId: string,
  columnIds: string[]
}
