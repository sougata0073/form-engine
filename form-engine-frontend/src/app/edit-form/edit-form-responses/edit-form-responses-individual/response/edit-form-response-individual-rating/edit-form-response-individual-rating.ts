import {Component, signal} from '@angular/core';
import {EditFormResponseIndividualComponent} from '../../../../../type/edit-form-response-individual-component';
import {RatingRes} from '../../../../../model/edit-form/question/response/rating-res';
import {RatingResponseIndividual} from '../../../../../model/edit-form/responses/individual/rating-response-individual';
import {FormsModule} from "@angular/forms";
import {NgOptimizedImage} from "@angular/common";
import {ArrayUtil} from '../../../../../util/array-util';
import {RatingIcon} from '../../../../../type/rating-icon';
import {kebabCase} from 'lodash';

@Component({
  selector: 'app-edit-form-response-individual-rating',
  imports: [
    FormsModule,
    NgOptimizedImage
  ],
  templateUrl: './edit-form-response-individual-rating.html',
  styleUrl: './edit-form-response-individual-rating.scss',
})
export class EditFormResponseIndividualRating extends EditFormResponseIndividualComponent<RatingRes, RatingResponseIndividual> {

  protected ratings = signal<number[]>([])

  override ngOnInit() {
    super.ngOnInit();

    this.ratings.set(ArrayUtil.fillByNumbers(1, this.question().maxRatingNumber))
  }

  protected ratingIconToPath(ratingIcon: RatingIcon, activated: boolean) {
    const pathPrefix = 'assets/images/rating-icons/'
    const suffix = activated ? 'activated' : 'deactivated'
    return `${pathPrefix}${kebabCase(ratingIcon)}-${suffix}.svg`
  }
}
