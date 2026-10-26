import { FormQuizReleaseMarksOption } from '../../type/form-quiz-release-marks.option';
import { PatchReq } from '../common/patch-req';

export interface FormQuizSettingsUpdateReq extends PatchReq {
  isQuiz?: boolean;
  releaseMarksOption?: FormQuizReleaseMarksOption;
  respondersCanSeeIncorrectAnswers?: boolean;
  respondersCanSeeCorrectAnswers?: boolean;
  respondersCanSeePointsReceived?: boolean;
  questionPointValue?: number;
}
