import {Component, signal} from '@angular/core';
import {EditFormResponseQuestionComponent} from '../../../../../type/edit-form-response-question-component';
import {
  RatingResponseQuestionRes,
  RatingResponseQuestionResResponse,
  RatingResponseQuestionResSummary
} from '../../../../../model/edit-form/responses/question/rating-response-question-res';
import {MatCard, MatCardContent} from '@angular/material/card';
import {ArrayUtil} from '../../../../../util/array-util';
import {RatingIcon} from '../../../../../type/rating-icon';
import {kebabCase} from 'lodash';
import {NgOptimizedImage} from '@angular/common';
import {
  EditFormResponseQuestionResponseContainer
} from '../../edit-form-response-question-response-container/edit-form-response-question-response-container';
import {InfiniteScrollList} from '../../../../../shared/infinite-scroll-list/infinite-scroll-list';
import {RatingRes} from '../../../../../model/edit-form/question/response/rating-res';

@Component({
  selector: 'app-edit-form-response-question-rating',
  imports: [
    MatCard,
    MatCardContent,
    NgOptimizedImage,
    EditFormResponseQuestionResponseContainer,
    InfiniteScrollList
  ],
  templateUrl: './edit-form-response-question-rating.html',
  styleUrl: './edit-form-response-question-rating.scss',
})
export class EditFormResponseQuestionRating extends EditFormResponseQuestionComponent<RatingResponseQuestionRes, RatingResponseQuestionResResponse, RatingRes> {

  protected ratingNums = signal<number[]>([])

  override ngOnInit() {
    super.ngOnInit();

    this.ratingNums.set(ArrayUtil.fillByNumbers(1, this.questionDetails().maxRatingNumber))
  }

  protected ratingIconToPath(ratingIcon: RatingIcon, suffix: 'activated' | 'deactivated' | 'disabled') {
    const pathPrefix = 'assets/images/rating-icons/'
    return `${pathPrefix}${kebabCase(ratingIcon)}-${suffix}.svg`
  }
}
