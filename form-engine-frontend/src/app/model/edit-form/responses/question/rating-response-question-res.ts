import {CommonResponseQuestionResponse} from './common-response-question-response';
import {ResponseQuestion} from './response-question';
import {RatingIcon} from '../../../../type/rating-icon';
import {ResponseQuestionSummary} from './response-question-summary';

export interface RatingResponseQuestionRes extends ResponseQuestion<RatingResponseQuestionResResponse> {

}

export interface RatingResponseQuestionResResponse extends CommonResponseQuestionResponse {
  rating: number | null
}

export interface RatingResponseQuestionResSummary extends ResponseQuestionSummary {
  ratingIcon: RatingIcon,
  maxRatingNumber: number
}
