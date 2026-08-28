import {Component, inject, input, OnInit, signal, viewChild,} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {MatIcon} from '@angular/material/icon';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';
import {MatTabLink, MatTabNav, MatTabNavPanel} from '@angular/material/tabs';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {MatCard, MatCardContent} from '@angular/material/card';
import {EditFormService} from '../../service/edit-form-service';
import {MatDialog} from '@angular/material/dialog';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {ReactiveFormsModule} from '@angular/forms';
import {PublishFormDialog} from '../../shared/publish-form-dialog/publish-form-dialog';
import {
  CopyResponderLinkMenuContent
} from '../../shared/copy-responder-link-menu-content/copy-responder-link-menu-content';
import {PublishedOptionsDialog} from '../../shared/published-options-dialog/published-options-dialog';
import {FunctionalDialog} from '../../shared/functional-dialog/functional-dialog';
import {AuthService} from '../../service/auth-service';
import {FormInfoRes} from '../../model/form/form-info-res';
import {filter} from 'rxjs';
import {EditFormStateService} from '../../service/edit-form-state-service';
import {InputDialog} from '../../shared/input-dialog/input-dialog';
import { FormService } from '../../service/form-service';

type TabLink = 'questions' | 'responses' | 'settings'

@Component({
  selector: 'app-edit-form-header',
  imports: [
    NgOptimizedImage,
    MatIcon,
    MatIconButton,
    MatTooltip,
    MatButton,
    MatTabNav,
    MatTabLink,
    MatTabNavPanel,
    MatCard,
    MatCardContent,
    MatMenu,
    MatMenuTrigger,
    ReactiveFormsModule,
    CopyResponderLinkMenuContent,
    MatMenuItem
  ],
  templateUrl: './edit-form-header.html',
  styleUrl: './edit-form-header.scss',
})
export class EditFormHeader implements OnInit {

  formId = input.required<string>()

  copyResponderLinkButton = viewChild<MatMenuTrigger>('copyResponderLinkButton');

  protected tabs = signal<{ label: string, link: TabLink }[]>([])
  protected activatedLink = signal<TabLink>('questions')

  private editFormService = inject(EditFormService)
  private formService = inject(FormService)
  protected editFormStateService = inject(EditFormStateService)
  private authService = inject(AuthService)
  private router = inject(Router)
  private activatedRoute = inject(ActivatedRoute)
  private dialog = inject(MatDialog)

  protected formInfo = this.editFormService.formInfo
  protected authJwtData = signal(this.authService.getJwtData())

  ngOnInit() {
    this.editFormService.loadFormInfo(this.formId(), () => {
      this.activatedRoute.queryParams.subscribe(val => {
        if (val['publishedOptions']) {
          if (this.formInfo()?.published) {
            this.openPublishedOptionsDialog()
          } else {
            this.openPublishFormDialog()
          }
        }
      })
    })

    this.tabs.set([
      {label: 'Questions', link: 'questions'},
      {label: 'Responses', link: 'responses'},
      {label: 'Settings', link: 'settings'}
    ])

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const activatedSection = this.router.url.split(/[/?]/)
          .find(s => this.tabs().map(t => t.link).includes(s as TabLink))

        this.activatedLink.set(activatedSection as TabLink)
      });

    const activatedSection = this.router.url.split(/[/?]/)
      .find(s => this.tabs().map(t => t.link).includes(s as TabLink))

    this.activatedLink.set(activatedSection as TabLink)
  }

  protected onTabClick(link: TabLink) {
    const paths: string[] = [link]
    if (link === 'responses') {
      paths.push('summary')
    }

    this.router.navigate(paths, {relativeTo: this.activatedRoute})
    this.activatedLink.set(link)
  }

  protected onFormsHomeClick() {
    this.router.navigate(['home'])
  }

  protected onPreviewClick() {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['forms', this.formInfo()!.id, 'preview'])
    )

    window.open(url, '_blank')
  }

  protected onPublishClick() {
    this.openPublishFormDialog()
  }

  protected openPublishFormDialog() {
    this.dialog.open(PublishFormDialog, {data: this.formInfo()}).afterClosed().subscribe((res: FormInfoRes) => {
      if (res.published) {
        this.copyResponderLinkButton()?.openMenu()
      }
    })
  }

  protected openPublishedOptionsDialog() {
    this.dialog.open(PublishedOptionsDialog, {
      data: this.formInfo()
    })
  }

  protected onPrintClick(moreMenuTrigger: MatMenuTrigger) {
    moreMenuTrigger.closeMenu()

    setTimeout(() => {
      window.print();
    }, 0)
  }

  protected onUnpublishFormClick() {
    if (!this.formInfo()!.published) return

    const dialogRef = this.dialog.open(FunctionalDialog, {
      data: FunctionalDialog.configure(
        'Unpublish form',
        'The form will no longer be visible to responders. Responders will see a blank page if they open the form link. Form editors can still make changes and publish the form again.',
        'Cancel',
        () => dialogRef.close(),
        'Unpublish',
        () => {
          const prevForm = this.formInfo()!
          this.editFormService.updateFormInfo(prevForm.id, {
            acceptingResponse: false,
            description: prevForm.description,
            notAcceptingResponseMessage: prevForm.notAcceptingResponseMessage,
            published: false,
            stopAcceptingResponseAfterResponse: null,
            stopAcceptingResponseOn: null,
            title: prevForm.title,
            name: prevForm.name
          }, () => {
            dialogRef.close()
          })
        }
      )
    })
  }

  protected onCopyFormClick() {

    const dialogRef = this.dialog.open(InputDialog, {
      data: InputDialog.configure(
        'Copy document',
        null,
        'Copy of ' + this.formInfo()?.name,
        'Cancel',
        () => {
          dialogRef.close()
        },
        'Make a copy',
        () => {
        }
      )
    });

    dialogRef.afterClosed().subscribe(result => {
      this.editFormStateService.showGlobalLoader.set(true)
      this.formService.copyForm(this.formId(), {formName: result}, res => {
          this.editFormStateService.showGlobalLoader.set(false)
          const url = `${window.location.origin}/forms/${res.id}/edit/questions`
          window.open(url, '_blank')
        }
      )
    });
  }

}
