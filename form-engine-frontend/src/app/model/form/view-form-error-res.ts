export interface ViewFormErrorRes {
  formTitle: string | null,
  reason: 'NOT_PUBLISHED' | 'NOT_ACCEPTING_RESPONSE' | 'RESPONSE_ALREADY_SUBMITTED',
  message: string
}
