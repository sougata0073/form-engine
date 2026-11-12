export interface FormGeneralSettings {
  published: boolean;
  acceptingResponse: boolean;
  responderNeedsToBeAuthenticated: boolean;
  notAcceptingResponseMessage: string | null;
  stopAcceptingResponseOn: string | null;
  responseLimit: number | null;
}
