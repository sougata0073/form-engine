import {Component, inject, input, OnInit, signal} from '@angular/core';
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';
import {MatTabLink, MatTabNav, MatTabNavPanel} from '@angular/material/tabs';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {ReactiveFormsModule} from '@angular/forms';
import {FormResponseService} from '../../../service/form-response-service';
import {filter} from 'rxjs';
import {FormResponseCount} from '../../../model/form/form-response-count';
import { FormResponseServiceShared } from '../../../service/form-response-service-shared';

type TabLink = 'summary' | 'question' | 'individual'

@Component({
  selector: 'app-edit-form-responses-header',
  imports: [
    MatCard,
    MatCardContent,
    MatIcon,
    MatIconButton,
    MatTooltip,
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    ReactiveFormsModule
  ],
  templateUrl: './edit-form-responses-header.html',
  styleUrl: './edit-form-responses-header.scss',
})
export class EditFormResponsesHeader implements OnInit {

  formId = input.required<string>()

  protected tabs = signal<{ label: string, link: TabLink }[]>([])
  protected activatedLink = signal<TabLink>('summary')

  private router = inject(Router)
  private activatedRoute = inject(ActivatedRoute)
  private formResponseServiceShared = inject(FormResponseServiceShared)

  protected formResponseCount = this.formResponseServiceShared.formResponseCount

  ngOnInit() {
    this.tabs.set([
      {label: 'Summary', link: 'summary'},
      {label: 'Question', link: 'question'},
      {label: 'Individual', link: 'individual'}
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
    if (this.activatedLink() !== link) {
      this.router.navigate([link], {relativeTo: this.activatedRoute})
      this.activatedLink.set(link)
    }
  }

}
