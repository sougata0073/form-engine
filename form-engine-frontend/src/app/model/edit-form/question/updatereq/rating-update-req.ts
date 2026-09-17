import { RatingIcon } from "../../../../type/rating-icon";
import { OnlyQuestionUpdateReq } from "./only-question-update-req";
import { QuestionUpdateReq } from "./question-update-req";

export interface RatingUpdateReq extends OnlyRatingUpdateReq, QuestionUpdateReq {

}

export interface OnlyRatingUpdateReq extends OnlyQuestionUpdateReq {
    maxRatingNumber?: number,
    ratingIcon?: RatingIcon
}