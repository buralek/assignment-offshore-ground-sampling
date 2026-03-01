import { Component, effect, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { SampleStatistics } from '../shared/models/statistics.model';
import { FilterService } from '../shared/services/filter.service';
import { StatisticsService } from '../shared/services/statistics.service';

@Component({
  selector: 'app-statistics',
  imports: [DecimalPipe, MatCardModule],
  templateUrl: './statistics.html',
  styleUrl: './statistics.css',
})
export class StatisticsComponent {
  private readonly filterService     = inject(FilterService);
  private readonly statisticsService = inject(StatisticsService);

  readonly statistics = signal<SampleStatistics | null>(null);

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