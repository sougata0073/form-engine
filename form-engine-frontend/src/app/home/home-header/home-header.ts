import {Component, inject, input, output, signal} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatTooltip} from "@angular/material/tooltip";
import {NgOptimizedImage} from "@angular/common";
import {AuthService} from '../../service/auth-service';

@Component({
  selector: 'app-home-header',
  imports: [
    FormsModule,
    MatCard,
    MatCardContent,
    MatIcon,
    MatIconButton,
    MatTooltip,
    NgOptimizedImage,
    ReactiveFormsModule
  ],
  templateUrl: './home-header.html',
  styleUrl: './home-header.scss',
})
export class HomeHeader {

  isTemplateGalleryOpened = input.required<boolean>()

  templateGalleryBackButtonClick = output<void>()
  search = output<string | null>()

  private authService = inject(AuthService)

  protected authJwtData = signal(this.authService.getJwtData())
  protected isSearchInputFocused = signal<boolean>(false)

  searchFormControl = new FormControl('')

  onSearch() {
    this.isSearchInputFocused.set(false)
    this.search.emit(this.searchFormControl.value)
  }

  onSearchInputKeyDown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.onSearch()
    }
  }

  onClearSearchClick() {
    this.searchFormControl.setValue(null)
  }

}
