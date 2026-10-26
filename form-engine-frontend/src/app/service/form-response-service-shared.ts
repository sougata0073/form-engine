import { HttpClient } from "@angular/common/http";
import { inject, Injectable, Signal, signal, WritableSignal } from "@angular/core";
import { FormResponseCount } from "../model/form/form-response-count";
import { FormInfo } from "../model/form/form-info";
import { FormDetails } from "../model/form/form-details";

@Injectable({
    providedIn: 'root',
})
export class FormResponseServiceShared {

    private _formResponseCount = signal<FormResponseCount | null>(null)
    formResponseCount = this._formResponseCount.asReadonly()

    private _formInfo = signal<FormInfo | null>(null)
    formInfo = this._formInfo.asReadonly()

    private _formDetails = signal<FormDetails | null>(null)
    formDetails = this._formDetails.asReadonly()

    private http = inject(HttpClient)

    getFormResponseCount(formId: string, onComplete: (res: FormResponseCount) => void) {
        const url = `form-responses/${formId}/form-response-count`

        this.http.get<FormResponseCount>(url).subscribe(res => {
            this._formResponseCount.set(res)
            onComplete(res)
        })
    }

    getFormInfo(formId: string, onComplete: (res: FormInfo) => void) {
        const url = `forms/${formId}/info`

        this.http.get<FormInfo>(url).subscribe(res => {
            this._formInfo.set(res)
            onComplete(res)
        })
    }

    getFormRes(formId: string, onComplete?: (res: FormDetails) => void) {
        const url = `forms/${formId}`

        this.http.get<FormDetails>(url).subscribe(res => {
            this._formDetails.set(res)
            onComplete?.(res)
        })
    }

    updateFormInfo(updateFn: (formInfo: WritableSignal<FormInfo | null>) => void) {
        updateFn(this._formInfo)
    }

    updateFormDetails(updateFn: (formDetails: WritableSignal<FormDetails | null>) => void) {
        updateFn(this._formDetails)
    }

}