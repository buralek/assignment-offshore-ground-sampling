import { Component, computed, effect, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { SampleStatistics } from '../shared/models/statistics.model';
import { UNIT_SYMBOLS } from '../shared/models/unit-system.model';
import { FilterService } from '../shared/services/filter.service';
import { StatisticsService } from '../shared/services/statistics.service';
import { UnitConversionService } from '../shared/services/unit-conversion.service';
import { UnitSystemService } from '../shared/services/unit-system.service';

@Component({
  selector: 'app-statistics',
  imports: [DecimalPipe, MatCardModule],
  templateUrl: './statistics.html',
  styleUrl: './statistics.css',
})
export class StatisticsComponent {
  private readonly filterService         = inject(FilterService);
  private readonly statisticsService     = inject(StatisticsService);
  private readonly unitSystemService     = inject(UnitSystemService);
  private readonly unitConversionService = inject(UnitConversionService);

  readonly statistics = signal<SampleStatistics | null>(null);

  readonly unitSymbols = computed(() => UNIT_SYMBOLS[this.unitSystemService.selected()]);

  readonly convertedThresholds = computed(() => {
    const t = this.statistics()?.thresholds;
    if (!t) return null;
    return {
      unitWeight:    this.unitConversionService.convertUnitWeight(t.unitWeight),
      waterContent:  t.waterContent,
      shearStrength: this.unitConversionService.convertShearStrength(t.shearStrength),
    };
  });

  constructor() {
    effect(() => {
      const locId = this.filterService.selectedLocationId();
      this.filterService.mutationVersion(); // track mutations
      this.statisticsService.get(locId).subscribe({
        next: stats => this.statistics.set(stats),
      });
    });
  }
}