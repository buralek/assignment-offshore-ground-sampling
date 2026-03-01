import { Component, computed, input } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { Sample } from '../shared/models/sample.model';

const THRESHOLDS = { unitWeight: 25, waterContent: 100, shearStrength: 800 };

@Component({
  selector: 'app-statistics',
  imports: [DecimalPipe, MatCardModule],
  templateUrl: './statistics.html',
  styleUrl: './statistics.css',
})
export class StatisticsComponent {
  readonly samples = input.required<Sample[]>();

  readonly stats = computed(() => {
    const samples = this.samples();
    const avg = samples.length
      ? samples.reduce((sum, s) => sum + s.waterContent, 0) / samples.length
      : 0;
    return {
      averageWaterContent: +avg.toFixed(1),
      exceeded: {
        unitWeight:    samples.filter(s => s.unitWeight    > THRESHOLDS.unitWeight).length,
        waterContent:  samples.filter(s => s.waterContent  > THRESHOLDS.waterContent).length,
        shearStrength: samples.filter(s => s.shearStrength > THRESHOLDS.shearStrength).length,
      },
    };
  });
}