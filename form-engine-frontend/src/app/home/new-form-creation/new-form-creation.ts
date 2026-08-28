import {Component, inject, input, output} from '@angular/core';
import {FormTemplate} from "../form-template/form-template";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatPrefix} from "@angular/material/input";
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {EditFormService} from '../../service/edit-form-service';
import {Router} from '@angular/router';
import {TemplateSummaryRes} from '../../model/template/template-summary-res';
import {HomeService} from '../../service/home-service';
import { FormService } from '../../service/form-service';

@Component({
  selector: 'app-new-form-creation',
  imports: [
    FormTemplate,
    MatButton,
    MatIcon,
    MatIconButton,
    MatPrefix,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger
  ],
  templateUrl: './new-form-creation.html',
  styleUrl: './new-form-creation.scss',
})
export class NewFormCreation {

  private formService = inject(FormService)
  private homeService = inject(HomeService)
  private router = inject(Router)

  headerTitle = input.required<string>()
  showControlButtons = input.required<boolean>()
  showBlankFormCreation = input.required<boolean>()
  templates = input.required<TemplateSummaryRes[]>()
  isRecentlyUsedTemplates = input.required<boolean>()

  templateGalleryClick = output<void>()
  hideTemplateGalleryClick = output<void>()

  openFormClick = output<void>()

  onTemplateGalleryClick() {
    if (this.showControlButtons()) {
      this.templateGalleryClick.emit()
    }
  }

  onHideTemplateGalleryClick() {
    if (this.showControlButtons()) {
      this.hideTemplateGalleryClick.emit()
    }
  }

  createBlankForm() {
    this.openFormClick.emit()
    this.formService.createForm({
      name: 'Untitled form',
      title: '',
      description: '',
      notAcceptingResponseMessage: 'This form is no longer accepting responses.',
      published: false,
      acceptingResponse: false,
      stopAcceptingResponseOn: null,
      stopAcceptingResponseAfterResponse: null
    }, (res) => {
      this.router.navigate(['forms', res.id, 'edit', 'questions'])
    })
  }

  buildFormFromTemplate(templateId: string) {
    this.openFormClick.emit()
    this.homeService.buildFormFromTemplate(templateId).subscribe(res => {
      this.router.navigate(['forms', res.formId, 'edit', 'questions'])
    })
  }

}
