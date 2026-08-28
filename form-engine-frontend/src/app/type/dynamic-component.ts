import {WritableSignal} from '@angular/core';

export interface DynamicComponent {
  componentId: WritableSignal<string>
}
