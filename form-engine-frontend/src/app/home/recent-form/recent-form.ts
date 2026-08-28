import {Component, inject, Inject, input, LOCALE_ID, OnInit, output, signal} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {FormSummaryRes} from '../../model/form/form-summary-res';
import {formatDate} from '@angular/common';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {InputDialog} from '../../shared/input-dialog/input-dialog';
import {HomeService} from '../../service/home-service';
import {SimpleDialog} from '../../shared/simple-dialog/simple-dialog';

@Component({
  selector: 'app-recent-form',
  imports: [
    MatIcon,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger
  ],
  templateUrl: './recent-form.html',
  styleUrl: './recent-form.scss',
})
export class RecentForm implements OnInit {

  form = input.required<FormSummaryRes>()

  showLoader = output<boolean>()
  deleteForm = output<string>()

  protected formattedDate = signal('')

  private homeService = inject(HomeService)
  private router = inject(Router)
  private dialog = inject(MatDialog);

  constructor(@Inject(LOCALE_ID) private locale: string) {
  }

  ngOnInit() {
    const formLastOpenedDate = new Date(this.form().lastOpenedOn)
    const today = new Date();

    const isToday =
      formLastOpenedDate.getFullYear() === today.getFullYear() &&
      formLastOpenedDate.getMonth() === today.getMonth() &&
      formLastOpenedDate.getDate() === today.getDate();

    if (isToday) {
      this.formattedDate.set(formatDate(formLastOpenedDate, 'HH:mm', this.locale))
    } else {
      this.formattedDate.set(formatDate(formLastOpenedDate, 'd MMM y', this.locale))
    }
  }

  openForm() {
    this.router.navigate(['forms', this.form().id, 'edit', 'questions'])
  }

  protected openRenameDialog() {
    const dialogRef = this.dialog.open(InputDialog, {
      data: InputDialog.configure(
        'Rename',
        'Please enter a new name for the item:',
        this.form().name,
        'Cancel',
        () => {
          dialogRef.close()
        },
        'OK',
        () => {
        }
      )
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined && this.form().name !== result) {
        this.showLoader.emit(true);
        this.homeService.renameForm(this.form().id, {newName: result}).subscribe(res => {
          this.form().name = result
          this.showLoader.emit(false);
        })
      }
    });

  }

  protected onRemoveFormClick() {
    const dialogRef = this.dialog.open(SimpleDialog, {
      data: SimpleDialog.configure(
        'Warning',
        'Deleting this form will permanently delete all of its questions and responses. This action cannot be undone',
        'Delete'
      )
    })

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.showLoader.emit(true);
        this.homeService.deleteForm(this.form().id).subscribe(res => {
          this.showLoader.emit(false);
          this.deleteForm.emit(this.form().id)
        })
      }
    })
  }

  protected onOpenInNewTabClick() {
    const url = `${window.location.origin}/forms/${this.form().id}/edit/questions`
    window.open(url, '_blank')
  }
}
