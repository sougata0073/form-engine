import {Component, computed, input} from '@angular/core';
import {MatCard, MatCardContent} from '@angular/material/card';

@Component({
  selector: 'app-question-card',
  imports: [
    MatCard,
    MatCardContent
  ],
  templateUrl: './question-card.html',
  styleUrl: './question-card.scss',
})
export class QuestionCard {
  isTopItem = input<boolean>(false)
  isFocused = input<boolean>(false)
  hasError = input<boolean>(false)

  protected cardClasses = computed(() => {
    return {
      'top-item': this.isTopItem(),
      'focused': this.isFocused(),
      'has-error': this.hasError()
    }
  })
}
