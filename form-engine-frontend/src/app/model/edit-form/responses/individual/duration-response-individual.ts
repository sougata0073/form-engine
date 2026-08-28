import {ResponseIndividual} from './response-individual';

export interface DurationResponseIndividual extends ResponseIndividual {
  hours: number,
  minutes: number,
  seconds: number
}
