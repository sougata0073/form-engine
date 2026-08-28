import {QuestionAddUpdateReq} from './question-add-update-req';
import {RatingIcon} from '../../../../type/rating-icon';

export interface OnlyRatingAddUpdateReq {
  maxRatingNumber: number,
  ratingIcon: RatingIcon
}

export interface RatingAddUpdateReq extends QuestionAddUpdateReq, OnlyRatingAddUpdateReq {
}

