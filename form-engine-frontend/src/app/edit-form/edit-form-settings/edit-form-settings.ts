import { Component, computed, inject, OnInit, signal, Signal } from '@angular/core';
import { MatCard, MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSlideToggle, MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatDivider } from '@angular/material/list';
import {
  MatExpansionModule,
  MatExpansionPanel,
  MatExpansionPanelHeader,
} from '@angular/material/expansion';
import { MatFormField, MatInputModule } from '@angular/material/input';
import { MatOption, MatSelect, MatSelectModule } from '@angular/material/select';
import { MatRadioButton, MatRadioGroup, MatRadioModule } from '@angular/material/radio';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormEmailCollectionOption } from '../../type/form-email-collection-option';
import { FormQuizReleaseMarksOption } from '../../type/form-quiz-release-marks.option';
import { ResponseCopySendOption } from '../../type/response-copy-send-option';
import { EditFormService } from '../../service/edit-form-service';
import { PatchReq } from '../../model/common/patch-req';
import { EditFormStateService } from '../../service/edit-form-state-service';
import { FormInfoUpdateReq } from '../../model/form/form-info-update-req';
import { FormDefaultSettings } from '../../model/form/form-default-settings';
import { FormDefaultSettingsUpdateReq } from '../../model/form/form-default-settings-update-req';

@Component({
  selector: 'app-edit-form-settings',
  imports: [
    MatCard,
    MatSlideToggle,
    MatDivider,
    MatExpansionPanel,
    MatExpansionPanelHeader,
    MatFormField,
    MatSelect,
    MatOption,
    MatRadioGroup,
    MatRadioButton,
    MatIcon,
    MatCardModule,
    MatDividerModule,
    MatSlideToggleModule,
    MatRadioModule,
    MatExpansionModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './edit-form-settings.html',
  styleUrl: './edit-form-settings.scss',
})
export class EditFormSettings implements OnInit {
  private formBuilder = inject(FormBuilder);
  private editFormService = inject(EditFormService);
  private editFormStateService = inject(EditFormStateService);

  protected formInfo = computed(() => this.editFormService.formInfo()!);
  protected formDefaultSettings = signal<FormDefaultSettings | null>(null);

  protected formGroup = this.formBuilder.nonNullable.group({
    quizSettings: this.formBuilder.nonNullable.group({
      isQuiz: false,
      releaseMarksOption: 'IMMEDIATELY' as FormQuizReleaseMarksOption,
      respondersCanSeeIncorrectAnswers: false,
      respondersCanSeeCorrectAnswers: false,
      respondersCanSeePointsReceived: false,
      questionPointValue: 0,
    }),
    responseSettings: this.formBuilder.nonNullable.group({
      emailCollectionOption: 'OFF' as FormEmailCollectionOption,
      responseCopySendOption: 'OFF' as ResponseCopySendOption,
      isResponseEditable: false,
      singleResponseOnly: false,
    }),
    presentationSettings: this.formBuilder.nonNullable.group({
      showProgressBar: false,
      shuffleQuestionOrder: false,
      responseSavedConfirmationMessage: 'Your response has been recorded',
      showLinkToSubmitAnotherResponse: false,
      shareResponseSummaryWithResponders: false,
      disableAutoSaveForAllRespondents: false,
    }),
    defaultSettings: this.formBuilder.nonNullable.group({
      defaultEmailCollectionOption: 'OFF' as FormEmailCollectionOption,
      makeQuestionsRequiredByDefault: false,
    }),
    editingConfirmation: false,
    responsesExpanded: false,
    presentationExpanded: false,
    formDefaultsExpanded: false,
    questionDefaultsExpanded: false,
  });

  private prevFormValues: typeof this.formGroup.value = {};

  ngOnInit(): void {
    this.editFormService.getFormDefaultSettings((res) => {
      this.formDefaultSettings.set(res);

      this.formGroup.patchValue(
        {
          defaultSettings: {
            defaultEmailCollectionOption: res.defaultEmailCollectionOption,
            makeQuestionsRequiredByDefault: res.makeQuestionsRequiredByDefault,
          },
        },
        { emitEvent: false },
      );
    });

    const presentationSettings = this.formInfo().presentationSettings;
    const quizSettings = this.formInfo().quizSettings;
    const responseSettings = this.formInfo().responseSettings;

    this.formGroup.patchValue({
      quizSettings: {
        isQuiz: quizSettings.isQuiz,
        releaseMarksOption: quizSettings.releaseMarksOption,
        respondersCanSeeIncorrectAnswers: quizSettings.respondersCanSeeIncorrectAnswers,
        respondersCanSeeCorrectAnswers: quizSettings.respondersCanSeeCorrectAnswers,
        respondersCanSeePointsReceived: quizSettings.respondersCanSeePointsReceived,
        questionPointValue: quizSettings.questionPointValue,
      },
      responseSettings: {
        emailCollectionOption: responseSettings.emailCollectionOption,
        responseCopySendOption: responseSettings.responseCopySendOption,
        isResponseEditable: responseSettings.isResponseEditable,
        singleResponseOnly: responseSettings.singleResponseOnly,
      },
      presentationSettings: {
        showProgressBar: presentationSettings.showProgressBar,
        shuffleQuestionOrder: presentationSettings.shuffleQuestionOrder,
        responseSavedConfirmationMessage: presentationSettings.responseSavedConfirmationMessage,
        showLinkToSubmitAnotherResponse: presentationSettings.showLinkToSubmitAnotherResponse,
        shareResponseSummaryWithResponders: presentationSettings.shareResponseSummaryWithResponders,
        disableAutoSaveForAllRespondents: presentationSettings.disableAutoSaveForAllRespondents,
      },
    });

    if (responseSettings.emailCollectionOption === 'OFF') {
      this.formGroup.controls.responseSettings.controls.responseCopySendOption.disable();
    }

    this.prevFormValues = this.formGroup.getRawValue();

    this.formGroup.controls.quizSettings.valueChanges.subscribe(() => {
      const val = this.formGroup.controls.quizSettings.getRawValue();

      this.editFormStateService.isFormGettingModified.set(true);

      const req: FormInfoUpdateReq = {
        quizSettingsUpdateReq: this.getFormInfoUpdateReq(val, this.prevFormValues.quizSettings),
        updateFields: ['quizSettingsUpdateReq' satisfies keyof FormInfoUpdateReq],
      };

      this.editFormService.updateFormInfo(this.formInfo().id, req, (res) => {
        this.editFormStateService.isFormGettingModified.set(false);
      });

      this.prevFormValues.quizSettings = val;
    });

    this.formGroup.controls.responseSettings.valueChanges.subscribe(() => {
      const val = this.formGroup.controls.responseSettings.getRawValue();

      this.editFormStateService.isFormGettingModified.set(true);

      const responseCopySendOptionControl =
        this.formGroup.controls.responseSettings.controls.responseCopySendOption;

      if (val.emailCollectionOption === 'OFF') {
        responseCopySendOptionControl.patchValue('OFF', { emitEvent: false });
        responseCopySendOptionControl.disable({ emitEvent: false });
      } else {
        responseCopySendOptionControl.enable({ emitEvent: false });
      }

      const req: FormInfoUpdateReq = {
        responseSettingsUpdateReq: this.getFormInfoUpdateReq(
          val,
          this.prevFormValues.responseSettings,
        ),
        updateFields: ['responseSettingsUpdateReq' satisfies keyof FormInfoUpdateReq],
      };

      this.editFormService.updateFormInfo(this.formInfo().id, req, (res) => {
        this.editFormStateService.isFormGettingModified.set(false);
      });

      this.prevFormValues.responseSettings = val;
    });

    this.formGroup.controls.presentationSettings.valueChanges.subscribe(() => {
      const val = this.formGroup.controls.presentationSettings.getRawValue();

      this.editFormStateService.isFormGettingModified.set(true);

      const req: FormInfoUpdateReq = {
        presentationSettingsUpdateReq: this.getFormInfoUpdateReq(
          val,
          this.prevFormValues.presentationSettings,
        ),
        updateFields: ['presentationSettingsUpdateReq' satisfies keyof FormInfoUpdateReq],
      };

      this.editFormService.updateFormInfo(this.formInfo().id, req, (res) => {
        this.editFormStateService.isFormGettingModified.set(false);
      });

      this.prevFormValues.presentationSettings = val;
    });

    this.formGroup.controls.defaultSettings.valueChanges.subscribe(() => {
      const val = this.formGroup.controls.defaultSettings.getRawValue();

      this.editFormStateService.isFormGettingModified.set(true);

      const req: FormDefaultSettingsUpdateReq = this.getFormInfoUpdateReq(
        val,
        this.prevFormValues.defaultSettings,
      );

      this.editFormService.updateFormDefaultSettings(req, (res) => {
        this.formDefaultSettings.set(res);
        this.editFormStateService.isFormGettingModified.set(false);
      });

      this.prevFormValues.defaultSettings = val;
    });

    this.formGroup.controls.defaultSettings.controls.defaultEmailCollectionOption.valueChanges.subscribe(
      (val) => {
        this.formGroup.controls.responseSettings.controls.emailCollectionOption.setValue(val);
      },
    );
  }

  protected startConfirmationEdit(): void {
    this.formGroup.controls.editingConfirmation.setValue(true);
  }

  protected saveConfirmation(value: string): void {
    this.formGroup.controls.presentationSettings.controls.responseSavedConfirmationMessage.setValue(
      value.trim(),
    );

    this.formGroup.controls.editingConfirmation.setValue(false);
  }

  protected cancelConfirmation(): void {
    this.formGroup.controls.presentationSettings.controls.responseSavedConfirmationMessage.setValue(
      this.formInfo().presentationSettings.responseSavedConfirmationMessage,
      { emitEvent: false },
    );

    this.formGroup.controls.editingConfirmation.setValue(false);
  }

  protected updateExpansionControl(control: FormControl<boolean>, expanded: boolean): void {
    control.setValue(expanded);
  }

  private getFormInfoUpdateReq(curr: any, prev: typeof curr): {} & PatchReq {
    let res = {};
    const changedFields: string[] = [];

    Object.keys(curr).forEach((key) => {
      const prevVal = prev[key];
      const currVal = curr[key];

      if (prevVal !== currVal) {
        res = { ...res, ...{ [key]: currVal } };
        changedFields.push(key);
      }
    });

    return { ...res, updateFields: changedFields };
  }
}
