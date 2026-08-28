import {Component, inject, OnInit, signal} from '@angular/core';
import {EditFormResponsesHeader} from './edit-form-responses-header/edit-form-responses-header';
import {ActivatedRoute, Router, RouterOutlet} from '@angular/router';
import {FormResponseService} from '../../service/form-response-service';
import {MatCard, MatCardContent} from '@angular/material/card';
import {FormResponseCount} from '../../model/form/form-response-count';
import { FormResponseServiceShared } from '../../service/form-response-service-shared';

@Component({
  selector: 'app-edit-form-responses',
  imports: [
    EditFormResponsesHeader,
    RouterOutlet,
    MatCard,
    MatCardContent
  ],
  templateUrl: './edit-form-responses.html',
  styleUrl: './edit-form-responses.scss',
})
export class EditFormResponses implements OnInit {

  formId = signal<string>('')

  protected formResponseServiceShared = inject(FormResponseServiceShared)

  protected formResponseCount = this.formResponseServiceShared.formResponseCount

  ngOnInit() {
  }

}
