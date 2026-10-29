import { Component, OnInit, Type } from '@angular/core';
import { MatCard, MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSlideToggle, MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatDivider } from '@angular/material/list';
import { MatExpansionModule, MatExpansionPanel, MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatFormField, MatInputModule } from '@angular/material/input';
import { MatOption, MatSelect, MatSelectModule } from '@angular/material/select';
import { MatRadioButton, MatRadioGroup, MatRadioModule } from '@angular/material/radio';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormEmailCollectionOption } from '../../type/form-email-collection-option';
import { FormQuizReleaseMarksOption } from '../../type/form-quiz-release-marks.option';
import { ResponseCopySendOption } from '../../type/response-copy-send-option';

type ResponseCopy = 'off' | 'when-requested' | 'always';

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
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-settings.html',
  styleUrl: './edit-form-settings.scss',
})
export class EditFormSettings implements OnInit {

  protected readonly settingsForm = new FormGroup({

    // Quiz
    isQuiz: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    releaseMarks: new FormControl<FormQuizReleaseMarksOption>('IMMEDIATELY', {
      nonNullable: true
    }),

    missedQuestions: new FormControl<boolean>(true, {
      nonNullable: true
    }),

    correctAnswers: new FormControl<boolean>(true, {
      nonNullable: true
    }),

    pointValues: new FormControl<boolean>(true, {
      nonNullable: true
    }),

    defaultQuestionPointValue: new FormControl<number>(0, {
      nonNullable: true
    }),

    // Responses
    collectEmailAddresses: new FormControl<FormEmailCollectionOption>(
      'RESPONDER_INPUT',
      {
        nonNullable: true
      }
    ),

    sendRespondersCopy: new FormControl<ResponseCopySendOption>('OFF', {
      nonNullable: true
    }),

    allowResponseEditing: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    limitToOneResponse: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    // Presentation
    showProgressBar: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    shuffleQuestionOrder: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    confirmationMessage: new FormControl<string>(
      'Your response has been recorded',
      {
        nonNullable: true
      }
    ),

    // UI state
    editingConfirmation: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    showAnotherResponseLink: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    viewResultsSummary: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    disableAutoSave: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    // Defaults
    collectEmailAddressesByDefault: new FormControl<FormEmailCollectionOption>(
      'RESPONDER_INPUT',
      {
        nonNullable: true
      }
    ),

    makeQuestionsRequiredByDefault: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    // Section expansion states
    responsesExpanded: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    presentationExpanded: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    formDefaultsExpanded: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    questionDefaultsExpanded: new FormControl<boolean>(false, {
      nonNullable: true
    })
  });

  protected readonly controls = this.settingsForm.controls;

  private readonly uiStateFormControlNames = [
    'responsesExpanded',
    'presentationExpanded',
    'formDefaultsExpanded',
    'questionDefaultsExpanded'
  ]

  private prevSettingsFormValues: typeof this.settingsForm.value = {}

  ngOnInit(): void {
    this.prevSettingsFormValues = this.settingsForm.value

    this.settingsForm.valueChanges.subscribe(val => {
      Object.keys(val).forEach(key => {
        if (this.uiStateFormControlNames.includes(key)) return

        const prevVal = (this.prevSettingsFormValues as any)[key]
        const currVal = (val as any)[key]

        if (prevVal !== currVal) {
          console.log('Key', key, 'Prev value', prevVal, 'Curr value', currVal)
        }
      })

      this.prevSettingsFormValues = val
    })
  }

  private confirmationMessageBackup =
    this.controls.confirmationMessage.value;

  protected startConfirmationEdit(): void {
    this.confirmationMessageBackup =
      this.controls.confirmationMessage.value;

    this.controls.editingConfirmation.setValue(true);
  }

  protected saveConfirmation(): void {
    const value =
      this.controls.confirmationMessage.value.trim();

    this.controls.confirmationMessage.setValue(value);

    this.confirmationMessageBackup = value;

    this.controls.editingConfirmation.setValue(false);
  }

  protected cancelConfirmation(): void {
    this.controls.confirmationMessage.setValue(
      this.confirmationMessageBackup
    );

    this.controls.editingConfirmation.setValue(false);
  }

  protected updateExpansionControl(
    control: FormControl<boolean>,
    expanded: boolean
  ): void {
    control.setValue(expanded);
  }

  protected submit(): void {
    console.log(
      this.settingsForm.getRawValue()
    );
  }

}
