import { Component, inject, OnInit, signal } from '@angular/core';
import { EditFormQuestionComponent } from '../../../../type/edit-form-question-component';
import { RatingRes } from '../../../../model/edit-form/question/response/rating-res';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatFormField } from "@angular/material/input";
import { MatOption } from "@angular/material/core";
import { MatSelect, MatSelectTrigger } from "@angular/material/select";
import { ArrayUtil } from '../../../../util/array-util';
import { NumberValidator } from '../../../../formValidator/number-validator';
import { RatingIcon } from '../../../../type/rating-icon';
import { RatingConstant } from '../../../../constant/rating-constant';
import { kebabCase } from 'lodash';
import { NgOptimizedImage } from '@angular/common';
import { EditFormStateService } from '../../../../service/edit-form-state-service';
import { OnlyRatingAddUpdateReq } from '../../../../model/edit-form/question/addreq/rating-add-req';
import { OnlyRatingUpdateReq, RatingUpdateReq } from '../../../../model/edit-form/question/updatereq/rating-update-req';

@Component({
  selector: 'app-edit-form-rating',
  imports: [
    FormsModule,
    MatFormField,
    MatOption,
    MatSelect,
    ReactiveFormsModule,
    NgOptimizedImage,
    MatSelectTrigger,
  ],
  templateUrl: './edit-form-rating.html',
  styleUrl: './edit-form-rating.scss',
})
export class EditFormRating extends EditFormQuestionComponent<RatingRes, OnlyRatingUpdateReq> implements OnInit {

  protected ratingNumbers = signal(ArrayUtil.fillByNumbers(3, 10))
  protected ratingIcons = signal(RatingConstant.RATING_ICONS)
  protected previewRatings = signal(ArrayUtil.fillByNumbers(1, 5))

  protected formGroup = new FormGroup({
    ratingNumber: new FormControl<number>(5, [Validators.required, NumberValidator.between(3, 10)]),
    ratingIcon: new FormControl<RatingIcon>('HEART', [Validators.required])
  })

  protected formStateService = inject(EditFormStateService)

  ngOnInit() {
    this.formGroup.patchValue({
      ratingNumber: this.question().maxRatingNumber,
      ratingIcon: this.question().ratingIcon
    })
    this.previewRatings.update(() => ArrayUtil.fillByNumbers(1, this.question().maxRatingNumber))

    this.emitCanSaveHasError()

    this.formGroup.valueChanges.subscribe(val => {
      this.previewRatings.update(() => ArrayUtil.fillByNumbers(1, val.ratingNumber ?? 1))

      this.emitCanSaveHasError()

      this.updateQuestion.emit(
        {
          maxRatingNumber: this.question().maxRatingNumber === val.ratingNumber ? undefined : val.ratingNumber!,
          ratingIcon: this.question().ratingIcon === val.ratingIcon ? undefined : val.ratingIcon!,
          updateFields: [
            this.question().maxRatingNumber === val.ratingNumber ? undefined : 'maxRatingNumber' satisfies keyof RatingUpdateReq,
            this.question().ratingIcon === val.ratingIcon ? undefined : 'ratingIcon' satisfies keyof RatingUpdateReq
          ].filter(field => field !== undefined)

        }
      )
    })
    this.formGroup.statusChanges.subscribe(() => this.emitCanSaveHasError())
  }

  protected ratingIconToPath(ratingIcon: RatingIcon, suffix: 'activated' | 'deactivated' | 'disabled') {
    const pathPrefix = 'assets/images/rating-icons/'
    return `${pathPrefix}${kebabCase(ratingIcon)}-${suffix}.svg`
  }

  private emitCanSaveHasError() {
    this.canSaveQuestion.emit(this.formGroup.valid)
    this.hasError.emit(this.formGroup.invalid)
  }
}
