import {Directive, input, OnInit} from '@angular/core';
import {ResponseIndividual} from '../model/edit-form/responses/individual/response-individual';
import {QuestionRes} from '../model/edit-form/question/response/question-res';

@Directive()
export class EditFormResponseIndividualComponent<QR extends QuestionRes, RI extends ResponseIndividual> implements OnInit{

  question = input.required<QR>()
  response = input<RI>()

  ngOnInit() {
  }

}
