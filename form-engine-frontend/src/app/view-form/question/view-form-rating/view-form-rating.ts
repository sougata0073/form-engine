import {Component, signal} from '@angular/core';
import {ViewFormQuestionComponent} from '../../../type/view-form-question-component';
import {RatingRes} from '../../../model/edit-form/question/response/rating-res';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {ArrayUtil} from '../../../util/array-util';
import {RatingIcon} from '../../../type/rating-icon';
import {kebabCase} from 'lodash';
import {NgOptimizedImage} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {OnlyRatingResponsePutReq} from '../../../model/view-form/request/rating-response-put-req';

@Component({
  selector: 'app-view-form-rating',
  imports: [
    ReactiveFormsModule,
    NgOptimizedImage,
    MatButton
  ],
  templateUrl: './view-form-rating.html',
  styleUrl: './view-form-rating.scss',
})
export class ViewFormRating extends ViewFormQuestionComponent<RatingRes, OnlyRatingResponsePutReq> {

  protected ratings = signal<number[]>([])
  protected currentHoveredRating = signal<number>(0)

  override formGroup = new FormGroup({
    rating: new FormControl<number | null>(null)
  })

  override ngOnInit() {
    super.ngOnInit();

    this.ratings.set(ArrayUtil.fillByNumbers(1, this.question().maxRatingNumber))
  }

  override getOnlyQuestionResponsePutReq(): OnlyRatingResponsePutReq | null {
    const value = this.formGroup.value.rating
    return value === null || value === undefined ? null : {
      rating: value
    }
  }

  protected ratingIconToPath(ratingIcon: RatingIcon, activated: boolean) {
    const pathPrefix = 'assets/images/rating-icons/'
    const suffix = activated ? 'activated' : 'deactivated'
    return `${pathPrefix}${kebabCase(ratingIcon)}-${suffix}.svg`
  }

  protected onRatingIconClick(ratingNum: number) {
    this.formGroup.controls.rating.patchValue(
      !this.question().required && this.formGroup.value.rating === ratingNum ? null : ratingNum
    )
  }

}
