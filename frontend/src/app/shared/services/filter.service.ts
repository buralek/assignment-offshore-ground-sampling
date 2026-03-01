import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class FilterService {
  readonly selectedLocationId = signal<string | null>(null);
  readonly mutationVersion    = signal(0);

  bumpMutation(): void {
    this.mutationVersion.update(v => v + 1);
  }
}