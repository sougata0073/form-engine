import {QuestionRes} from './question-res';
import {RatingIcon} from '../../../../type/rating-icon';

export interface OnlyRatingRes {
  maxRatingNumber: number,
  ratingIcon: RatingIcon
}

export interface RatingRes extends OnlyRatingRes, QuestionRes {
}

