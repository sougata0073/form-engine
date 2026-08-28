import {Component} from '@angular/core';
import {MatCard, MatCardContent, MatCardModule} from "@angular/material/card";
import {MatCheckbox, MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonToggle} from '@angular/material/button-toggle';
import {MatSlideToggle, MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatDivider} from '@angular/material/list';
import {MatExpansionModule, MatExpansionPanel, MatExpansionPanelHeader} from '@angular/material/expansion';
import {MatFormField, MatInputModule} from '@angular/material/input';
import {MatOption, MatSelect, MatSelectModule} from '@angular/material/select';
import {MatRadioButton, MatRadioGroup, MatRadioModule} from '@angular/material/radio';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatDividerModule} from '@angular/material/divider';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatButtonModule} from '@angular/material/button';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';

type ReleaseMarks = 'immediately' | 'later';

type EmailCollection = 'responder-input' | 'verified' | 'off';

type ResponseCopy = 'off' | 'when-requested' | 'always';

@Component({
  selector: 'app-edit-form-settings',
  imports: [
    MatCard,
    MatCheckbox,
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
export class EditFormSettings {

  protected readonly settingsForm = new FormGroup({

    /* ==========================================================
       QUIZ
    ========================================================== */

    isQuiz: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    releaseMarks: new FormControl<ReleaseMarks>('immediately', {
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


    /* ==========================================================
       RESPONSES
    ========================================================== */

    collectEmailAddresses: new FormControl<EmailCollection>(
      'responder-input',
      {
        nonNullable: true
      }
    ),

    sendRespondersCopy: new FormControl<ResponseCopy>('off', {
      nonNullable: true
    }),

    allowResponseEditing: new FormControl<boolean>(false, {
      nonNullable: true
    }),

    limitToOneResponse: new FormControl<boolean>(false, {
      nonNullable: true
    }),


    /* ==========================================================
       PRESENTATION
    ========================================================== */

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

    /*
     * UI state for confirmation editor.
     */
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


    /* ==========================================================
       DEFAULTS
    ========================================================== */

    collectEmailAddressesByDefault: new FormControl<EmailCollection>(
      'responder-input',
      {
        nonNullable: true
      }
    ),

    makeQuestionsRequiredByDefault: new FormControl<boolean>(false, {
      nonNullable: true
    }),


    /* ==========================================================
       EXPANSION STATES
    ========================================================== */

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


  /*
   * Stores the last saved confirmation message so Cancel
   * can restore it.
   */
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
