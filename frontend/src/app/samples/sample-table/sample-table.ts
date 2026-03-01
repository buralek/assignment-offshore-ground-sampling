import { Component, computed, input, model, signal } from '@angular/core';
import { UnitToggleComponent } from '../unit-toggle/unit-toggle';
import { DatePipe, DecimalPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Location } from '../../shared/models/location.model';
import { Sample } from '../../shared/models/sample.model';
import { UnitSystem, UNIT_LABELS } from '../../shared/models/unit-system.model';
import { MOCK_SAMPLE_PAGE } from '../../shared/mock-data';

const THRESHOLDS = { unitWeight: 25, waterContent: 100, shearStrength: 800 };

@Component({
  selector: 'app-sample-table',
  imports: [
    DatePipe, DecimalPipe,
    MatButtonModule, MatCardModule, MatFormFieldModule,
    MatIconModule, MatProgressSpinnerModule,
    MatSelectModule, MatTableModule, MatTooltipModule,
    UnitToggleComponent,
  ],
  templateUrl: './sample-table.html',
  styleUrl: './sample-table.css',
})
export class SampleTableComponent {
  readonly unitSystem    = signal<UnitSystem>('metric');
  readonly locations     = input.required<Location[]>();
  readonly samples       = input.required<Sample[]>();
  readonly selectedLocationId = model<string | null>(null);

  private readonly loading = signal(false);
  private readonly error   = signal(false);
  readonly hasMore = signal(MOCK_SAMPLE_PAGE.hasMore);

  readonly displayColumns = ['id', 'location', 'date', 'unitWeight', 'waterContent', 'shearStrength', 'actions'];

  readonly columnLabels = computed(() => UNIT_LABELS[this.unitSystem()]);

  readonly exceededIds = computed(() => {
    const s = this.samples();
    return {
      unitWeight:    new Set(s.filter(x => x.unitWeight    > THRESHOLDS.unitWeight).map(x => x.id)),
      waterContent:  new Set(s.filter(x => x.waterContent  > THRESHOLDS.waterContent).map(x => x.id)),
      shearStrength: new Set(s.filter(x => x.shearStrength > THRESHOLDS.shearStrength).map(x => x.id)),
    };
  });

  readonly tableState = computed<'loading' | 'loaded' | 'empty' | 'error'>(() => {
    if (this.loading()) return 'loading';
    if (this.error())   return 'error';
    return this.samples().length === 0 ? 'empty' : 'loaded';
  });

  getLocationName(locationId: string): string {
    return this.locations().find(l => l.id === locationId)?.name ?? locationId;
  }

  convertUnitWeight(v: number): number {
    return this.unitSystem() === 'us' ? +(v * 6.36587).toFixed(2) : v;
  }

  convertShearStrength(v: number): number {
    return this.unitSystem() === 'us' ? +(v * 20.8854).toFixed(2) : v;
  }

  isAnyExceeded(sample: Sample): boolean {
    const e = this.exceededIds();
    return e.unitWeight.has(sample.id) || e.waterContent.has(sample.id) || e.shearStrength.has(sample.id);
  }

  openAddDialog(): void             { /* TODO */ }
  openEditDialog(_s: Sample): void  { /* TODO */ }
  openDeleteDialog(_s: Sample): void { /* TODO */ }
  retry(): void                      { /* TODO */ }
  loadMore(): void                   { /* TODO */ }
}