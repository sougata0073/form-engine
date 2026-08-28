import { AfterContentInit, AfterViewInit, Component, ElementRef, input, OnDestroy, output, viewChild } from '@angular/core';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-infinite-scroll-list',
  imports: [
    MatProgressSpinner
  ],
  templateUrl: './infinite-scroll-list.html',
  styleUrl: './infinite-scroll-list.scss',
})
export class InfiniteScrollList implements OnDestroy, AfterViewInit {

  rootMargin = input<string>('0px 0px 200px 0px')
  listStyle = input<Record<string, string>>()
  showItemLoader = input<boolean>(false)
  isNextItemsLoading = input<boolean>(false)
  isScrollContainerImmediateParent = input(false)

  lastItemVisible = output<void>()

  scrollContainer = viewChild<ElementRef<HTMLElement>>('scrollContainer')
  scrollAnchor = viewChild<ElementRef<HTMLElement>>('scrollAnchorVertical')

  protected intersectionObserver?: IntersectionObserver

  ngAfterViewInit() {

    const scrollParent = this.isScrollContainerImmediateParent() ?
      this.scrollContainer()?.nativeElement :
      this.findScrollParent(this.scrollContainer()?.nativeElement as HTMLElement);

    this.intersectionObserver = new IntersectionObserver(
      (entries) => {
        const scrollAnchor = entries[0];

        if (scrollAnchor.isIntersecting) {
          this.lastItemVisible.emit();
        }
      },
      {
        root: scrollParent,
        rootMargin: this.rootMargin(),
        threshold: 0
      }
    );

    const scrollAnchor = this.scrollAnchor()?.nativeElement

    if (scrollAnchor) {
      this.intersectionObserver?.observe(scrollAnchor)
    }
  }

  ngOnDestroy() {
    this.intersectionObserver?.disconnect()
  }

  private findScrollParent(element: HTMLElement): HTMLElement | null {
    let parent = element.parentElement;

    while (parent) {
      const style = getComputedStyle(parent);

      const isScrollable =
        ['auto', 'scroll'].includes(style.overflowY)

      if (isScrollable) {
        return parent;
      }

      parent = parent.parentElement;
    }

    return null;
  }

}
