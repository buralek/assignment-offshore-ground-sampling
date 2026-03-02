import { Component, computed, effect, inject, signal } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { Sample } from '../shared/models/sample.model';
import { UNIT_LABELS } from '../shared/models/unit-system.model';
import { FilterService } from '../shared/services/filter.service';
import { SampleService } from '../shared/services/sample.service';
import { UnitConversionService } from '../shared/services/unit-conversion.service';
import { UnitSystemService } from '../shared/services/unit-system.service';

@Component({
  selector: 'app-graph',
  imports: [MatCardModule, MatButtonToggleModule, BaseChartDirective],
  templateUrl: './graph.html',
  styleUrl: './graph.css',
})
export class GraphComponent {
  private readonly filterService         = inject(FilterService);
  private readonly sampleService         = inject(SampleService);
  private readonly unitSystemService     = inject(UnitSystemService);
  private readonly unitConversionService = inject(UnitConversionService);

  readonly activeParam = signal<'unitWeight' | 'waterContent' | 'shearStrength'>('unitWeight');
  private readonly allSamples = signal<Sample[]>([]);

  constructor() {
    effect(() => {
      const locId = this.filterService.selectedLocationId();
      this.filterService.mutationVersion();
      this.sampleService.getAll(locId).subscribe(s => this.allSamples.set(s));
    });
  }

  readonly chartData = computed<ChartData<'line'>>(() => {
    const param  = this.activeParam();
    const unit   = this.unitSystemService.selected();
    const sorted = [...this.allSamples()].sort((a, b) => a.depth - b.depth);
    const values = sorted.map(s => {
      if (param === 'unitWeight')    return this.unitConversionService.convertUnitWeight(s.unitWeight);
      if (param === 'shearStrength') return this.unitConversionService.convertShearStrength(s.shearStrength);
      return s.waterContent;
    });
    return {
      labels: sorted.map(s => s.depth.toFixed(1)),
      datasets: [{
        label: UNIT_LABELS[unit][param],
        data: values,
        borderColor: '#1976d2',
        backgroundColor: 'rgba(25,118,210,0.08)',
        tension: 0.3,
        fill: true,
        pointRadius: 4,
      }],
    };
  });

  readonly chartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: { title: { display: true, text: 'Depth (m)' } },
      y: { title: { display: true, text: 'Value' } },
    },
  };
}