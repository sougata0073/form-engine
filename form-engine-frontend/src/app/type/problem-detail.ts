import { FormDetailsMayChangedErrorReason } from "../constant/form-details-may-changed-error-reason";
import { ProblemDetailTitle } from "../constant/problem-detail-title";

export interface ProblemDetail {
    detail: FormDetailsMayChangedErrorReason,
    instance: string,
    status: number,
    title: ProblemDetailTitle
}