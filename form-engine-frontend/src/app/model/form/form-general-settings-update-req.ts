import { PatchReq } from "../common/patch-req";

export interface FormGeneralSettingsUpdateReq extends PatchReq {
    published?: boolean,
    acceptingResponse?: boolean,
    notAcceptingResponseMessage?: string | null,
    stopAcceptingResponseOn?: string | null,
    responseLimit?: number | null
}