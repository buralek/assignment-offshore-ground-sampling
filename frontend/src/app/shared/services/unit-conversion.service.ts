import { inject, Injectable } from '@angular/core';
import { UNIT_CONVERSIONS } from '../models/unit-system.model';
import { UnitSystemService } from './unit-system.service';

@Injectable({ providedIn: 'root' })
export class UnitConversionService {
  private readonly unitSystemService = inject(UnitSystemService);

  convertUnitWeight(v: number): number {
    return +(v * UNIT_CONVERSIONS[this.unitSystemService.selected()].unitWeight).toFixed(2);
  }

  convertShearStrength(v: number): number {
    return +(v * UNIT_CONVERSIONS[this.unitSystemService.selected()].shearStrength).toFixed(2);
  }

  convertDepth(v: number): number {
    return +(v * UNIT_CONVERSIONS[this.unitSystemService.selected()].depth).toFixed(2);
  }
}
