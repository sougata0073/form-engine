export interface FormGeneralSettings {
    published: boolean,
    acceptingResponse: boolean,
    notAcceptingResponseMessage: string | null,
    stopAcceptingResponseOn: string | null,
    responseLimit: number | null
}