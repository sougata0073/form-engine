import {Component, signal} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  LinearScaleResponseQuestionRes,
  LinearScaleResponseQuestionResResponse,
  LinearScaleResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/linear-scale-response-question-res';
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {ArrayUtil} from '../../../../../util/array-util';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {LinearScaleRes} from '../../../../../model/edit-form/question/response/linear-scale-res';

@Component({
  selector: 'app-edit-form-response-question-linear-scale',
  imports: [
    MatCard,
    MatCardContent,
    MatIcon,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-linear-scale.html',
  styleUrl: './edit-form-response-question-linear-scale.scss',
})
export class EditFormResponseQuestionLinearScale extends EditFormResponseQuestionComponent<LinearScaleResponseQuestionRes, LinearScaleResponseQuestionResResponse, LinearScaleRes> {

  protected scaleNums = signal<number[]>([])

  override ngOnInit() {
    super.ngOnInit();

    this.scaleNums.set(ArrayUtil.fillByNumbers(this.questionDetails().fromNumber, this.questionDetails().toNumber))
  }

}
