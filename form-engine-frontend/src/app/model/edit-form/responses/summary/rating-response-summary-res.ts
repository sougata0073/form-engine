import {ResponseSummary} from './response-summary';
import {RatingIcon} from '../../../../type/rating-icon';

export interface RatingResponseSummaryRes extends ResponseSummary {
  averageRating: string,
  ratingIcon: RatingIcon,
  maxRatingNumber: number,
  responses: Response[]
}

export interface Response {
  rating: number,
  responseCount: string
}
