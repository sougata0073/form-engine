import { FormQuizReleaseMarksOption } from '../../type/form-quiz-release-marks.option';

export interface FormQuizSettings {
  isQuiz: boolean;
  releaseMarksOption: FormQuizReleaseMarksOption;
  respondersCanSeeIncorrectAnswers: boolean;
  respondersCanSeeCorrectAnswers: boolean;
  respondersCanSeePointsReceived: boolean;
  questionPointValue: number;
}
