import {Component, inject, OnInit, signal} from '@angular/core';
import {MatButton, MatIconButton} from '@angular/material/button';
import {ViewFormInfo} from '../view-form/view-form-info/view-form-info';
import {ViewFormQuestionWrapper} from '../view-form/view-form-question-wrapper/view-form-question-wrapper';
import {MatCard, MatCardContent} from '@angular/material/card';
import {ActivatedRoute, Router} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {MatTooltip} from '@angular/material/tooltip';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {Title} from '@angular/platform-browser';
import {EditFormService} from '../service/edit-form-service';
import {MatMenu, MatMenuTrigger} from '@angular/material/menu';
import {
  CopyResponderLinkMenuContent
} from '../shared/copy-responder-link-menu-content/copy-responder-link-menu-content';
import {MatDialog} from '@angular/material/dialog';
import {ViewFormFooter} from '../shared/view-form-footer/view-form-footer';
import {BehaviorSubject} from 'rxjs';
import {FunctionalDialog} from '../shared/functional-dialog/functional-dialog';

@Component({
  selector: 'app-preview-form',
  imports: [
    MatButton,
    ViewFormInfo,
    ViewFormQuestionWrapper,
    MatCard,
    MatCardContent,
    MatIcon,
    MatIconButton,
    MatTooltip,
    MatProgressSpinner,
    MatMenuTrigger,
    MatMenu,
    CopyResponderLinkMenuContent,
    ViewFormFooter
  ],
  templateUrl: './preview-form.html',
  styleUrl: './preview-form.scss',
})
export class PreviewForm implements OnInit {

  formId = signal<string>('')

  protected editFormService = inject(EditFormService)
  protected activatedRoute = inject(ActivatedRoute)
  protected router = inject(Router)
  protected title = inject(Title)
  protected dialog = inject(MatDialog)

  protected _clearClick = new BehaviorSubject<boolean>(false)
  protected clearClick = this._clearClick.asObservable()

  protected formRes = this.editFormService.formRes

  ngOnInit() {
    this.activatedRoute.parent!.paramMap.subscribe(params => {

      this.formId.set(params.get('formId')!);

      this.editFormService.loadFormRes(this.formId(), (res) => {
        this.title.setTitle(res.title ?? 'Form engine')
      })
    });
  }

  protected onCLoseClick() {
    this.router.navigate(['forms', this.formId(), 'edit'])
  }

  protected onManagePublishSettingsClick() {
    this.router.navigate(
      ['forms', this.formId(), 'edit', 'questions'], {queryParams: {'publishedOptions': 1}}
    )
  }

  protected onClearClick() {
    const dialogRef = this.dialog.open(FunctionalDialog, {
      data: FunctionalDialog.configure(
        'Clear form?',
        'This will remove your answers from all questions and cannot be undone.',
        'Cancel',
        () => dialogRef.close(),
        'Clear form',
        () => {
          this._clearClick.next(true)
          dialogRef.close()
        }
      )
    })
  }
}
