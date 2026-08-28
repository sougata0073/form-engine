import {AnyResponseIndividual} from '../../../../type/any-response-individual';

export interface ResponseIndividualRes {
  formResponseId: string,
  page: string,
  userId: string,
  responses: AnyResponseIndividual[]
}
