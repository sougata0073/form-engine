import { PatchReq } from "../common/patch-req";

export interface FormPresentationSettingsUpdateReq extends PatchReq {
  showProgressBar?: boolean;
  shuffleQuestionOrder?: boolean;
  responseSavedConfirmationMessage?: string | null;
  showLinkToSubmitAnotherResponse?: boolean;
  shareResponseSummaryWithResponders?: boolean;
  disableAutoSaveForAllRespondents?: boolean;
}
