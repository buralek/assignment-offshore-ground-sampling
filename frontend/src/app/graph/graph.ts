import { Component, computed, effect, inject, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { Sample } from '../shared/models/sample.model';
import { UNIT_LABELS } from '../shared/models/unit-system.model';
import { FilterService } from '../shared/services/filter.service';
import { SampleService } from '../shared/services/sample.service';
import { UnitConversionService } from '../shared/services/unit-conversion.service';
import { UnitSystemService } from '../shared/services/unit-system.service';
import { UnitToggleComponent } from '../shared/components/unit-toggle/unit-toggle';

const DATASET_STYLES = {
  unitWeight:    { borderColor: '#1976d2', backgroundColor: 'rgba(25,118,210,0.08)', tension: 0.3, fill: false, pointRadius: 4 },
  waterContent:  { borderColor: '#388e3c', backgroundColor: 'rgba(56,142,60,0.08)',  tension: 0.3, fill: false, pointRadius: 4 },
  shearStrength: { borderColor: '#f57c00', backgroundColor: 'rgba(245,124,0,0.08)', tension: 0.3, fill: false, pointRadius: 4 },
} as const;

@Component({
  selector: 'app-graph',
  imports: [MatCardModule, BaseChartDirective, UnitToggleComponent],
  templateUrl: './graph.html',
  styleUrl: './graph.css',
})
export class GraphComponent {
  private readonly filterService         = inject(FilterService);
  private readonly sampleService         = inject(SampleService);
  private readonly unitSystemService     = inject(UnitSystemService);
  private readonly unitConversionService = inject(UnitConversionService);

  private readonly allSamples = signal<Sample[]>([]);

  constructor() {
    effect(() => {
      const locId = this.filterService.selectedLocationId();
      this.filterService.mutationVersion();
      this.sampleService.getAll(locId).subscribe(s => this.allSamples.set(s));
    });
  }

  private readonly sorted = computed(() =>
    [...this.allSamples()].sort((a, b) => a.depth - b.depth)
  );

  private readonly xLabels = computed(() =>
    this.sorted().map(s => s.depth.toFixed(1))
  );

  readonly unitWeightChartData = computed<ChartData<'line'>>(() => ({
    labels: this.xLabels(),
    datasets: [{
      ...DATASET_STYLES.unitWeight,
      label: UNIT_LABELS[this.unitSystemService.selected()]['unitWeight'],
      data: this.sorted().map(s => this.unitConversionService.convertUnitWeight(s.unitWeight)),
    }],
  }));

  readonly waterContentChartData = computed<ChartData<'line'>>(() => ({
    labels: this.xLabels(),
    datasets: [{
      ...DATASET_STYLES.waterContent,
      label: UNIT_LABELS[this.unitSystemService.selected()]['waterContent'],
      data: this.sorted().map(s => s.waterContent),
    }],
  }));

  readonly shearStrengthChartData = computed<ChartData<'line'>>(() => ({
    labels: this.xLabels(),
    datasets: [{
      ...DATASET_STYLES.shearStrength,
      label: UNIT_LABELS[this.unitSystemService.selected()]['shearStrength'],
      data: this.sorted().map(s => this.unitConversionService.convertShearStrength(s.shearStrength)),
    }],
  }));

  readonly unitWeightOptions    = computed<ChartOptions<'line'>>(() => this.buildOptions(UNIT_LABELS[this.unitSystemService.selected()]['unitWeight']));
  readonly waterContentOptions  = computed<ChartOptions<'line'>>(() => this.buildOptions(UNIT_LABELS[this.unitSystemService.selected()]['waterContent']));
  readonly shearStrengthOptions = computed<ChartOptions<'line'>>(() => this.buildOptions(UNIT_LABELS[this.unitSystemService.selected()]['shearStrength']));

  private buildOptions(yTitle: string): ChartOptions<'line'> {
    return {
      responsive: true,
      maintainAspectRatio: false,
      plugins: { legend: { display: false } },
      scales: {
        x: { title: { display: true, text: 'Depth (m)' } },
        y: { title: { display: true, text: yTitle } },
      },
    };
  }
}