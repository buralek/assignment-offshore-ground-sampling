import { Injectable, signal } from '@angular/core';
import { UnitSystem } from '../models/unit-system.model';

@Injectable({ providedIn: 'root' })
export class UnitSystemService {
  readonly selected = signal<UnitSystem>('metric');
}