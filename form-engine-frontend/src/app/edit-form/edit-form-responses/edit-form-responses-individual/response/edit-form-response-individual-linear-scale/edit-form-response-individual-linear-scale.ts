import {Component, signal} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {
  LinearScaleResponseIndividual
} from '../../../../../model/edit-form/responses/individual/linear-scale-response-individual';
import {LinearScaleRes} from '../../../../../model/edit-form/question/response/linear-scale-res';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {ArrayUtil} from '../../../../../util/array-util';

@Component({
  selector: 'app-edit-form-response-individual-linear-scale',
  imports: [
    FormsModule,
    MatRadioButton,
    MatRadioGroup,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-response-individual-linear-scale.html',
  styleUrl: './edit-form-response-individual-linear-scale.scss',
})
export class EditFormResponseIndividualLinearScale extends EditFormResponseIndividualComponent<LinearScaleRes, LinearScaleResponseIndividual> {

  protected buttons = signal<number[]>([])

  protected formGroup = new FormGroup({
    linearScale: new FormControl<number | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.formGroup.controls.linearScale.disable()

    this.buttons.set(ArrayUtil.fillByNumbers(this.question().fromNumber, this.question().toNumber))

    var scaleResponse = this.response()?.scale

    if (scaleResponse) {
      this.formGroup.controls.linearScale.setValue(scaleResponse)
    }
  }

}
