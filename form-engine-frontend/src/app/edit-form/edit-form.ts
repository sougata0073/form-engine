import {Component, inject, input, OnInit} from '@angular/core';
import {EditFormHeader} from './edit-form-header/edit-form-header';
import {RouterOutlet} from '@angular/router';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {EditFormStateService} from '../service/edit-form-state-service';
import {CdkScrollable} from '@angular/cdk/overlay';

@Component({
  selector: 'app-edit-form',
  imports: [
    EditFormHeader,
    RouterOutlet,
    MatProgressSpinner,
    CdkScrollable,
  ],
  templateUrl: './edit-form.html',
  styleUrl: './edit-form.scss',
})
export class EditForm implements OnInit {

  formId = input.required<string>()

  protected editFormStateService = inject(EditFormStateService)

  ngOnInit() {

  }

}
