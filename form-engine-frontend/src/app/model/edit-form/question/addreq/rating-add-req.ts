import {QuestionAddReq} from './question-add-req';
import {RatingIcon} from '../../../../type/rating-icon';

export interface OnlyRatingAddUpdateReq {
  maxRatingNumber: number,
  ratingIcon: RatingIcon
}

export interface RatingAddUpdateReq extends QuestionAddReq, OnlyRatingAddUpdateReq {
}

