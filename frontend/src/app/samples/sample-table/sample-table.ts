import { Component, computed, effect, inject, signal } from '@angular/core';
import { LocationFilterComponent } from '../../shared/components/location-filter/location-filter';
import { UnitToggleComponent } from '../../shared/components/unit-toggle/unit-toggle';
import { DatePipe, DecimalPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Location } from '../../shared/models/location.model';
import { Sample, SampleCursor, SampleRequest } from '../../shared/models/sample.model';
import { UNIT_LABELS } from '../../shared/models/unit-system.model';
import { SampleStatistics } from '../../shared/models/statistics.model';
import { FilterService } from '../../shared/services/filter.service';
import { LocationService } from '../../shared/services/location.service';
import { SampleService } from '../../shared/services/sample.service';
import { StatisticsService } from '../../shared/services/statistics.service';
import { UnitConversionService } from '../../shared/services/unit-conversion.service';
import { UnitSystemService } from '../../shared/services/unit-system.service';
import { SampleFormDialogComponent } from '../sample-form-dialog/sample-form-dialog';
import { DeleteConfirmDialogComponent } from '../delete-confirm-dialog/delete-confirm-dialog';

@Component({
  selector: 'app-sample-table',
  imports: [
    DatePipe, DecimalPipe,
    MatButtonModule, MatCardModule,
    MatIconModule, MatProgressSpinnerModule,
    MatTableModule, MatTooltipModule,
    LocationFilterComponent, UnitToggleComponent,
  ],
  templateUrl: './sample-table.html',
  styleUrl: './sample-table.css',
})
export class SampleTableComponent {
  private readonly dialog               = inject(MatDialog);
  private readonly locationService      = inject(LocationService);
  private readonly sampleService        = inject(SampleService);
  private readonly statisticsService    = inject(StatisticsService);
  readonly filterService                = inject(FilterService);
  readonly unitSystemService            = inject(UnitSystemService);
  readonly unitConversionService        = inject(UnitConversionService);

  readonly locations  = signal<Location[]>([]);
  readonly samples    = signal<Sample[]>([]);
  readonly statistics = signal<SampleStatistics | null>(null);
  readonly hasMore    = signal(false);
  readonly loading    = signal(false);
  readonly error      = signal(false);
  private  nextCursor = signal<SampleCursor | null>(null);

  constructor() {
    this.locationService.getAll().subscribe(locs => this.locations.set(locs));

    effect(() => {
      const locId = this.filterService.selectedLocationId();
      this.samples.set([]);
      this.nextCursor.set(null);
      this.loadPage(locId, null);
    });

    effect(() => {
      const locId = this.filterService.selectedLocationId();
      this.filterService.mutationVersion();
      this.statisticsService.get(locId).subscribe({
        next: stats => this.statistics.set(stats),
      });
    });
  }

  private loadPage(locId: string | null, cursor: SampleCursor | null): void {
    this.loading.set(true);
    this.error.set(false);
    this.sampleService.getPage({ locationId: locId, cursor }).subscribe({
      next: page => {
        this.samples.update(prev => [...prev, ...page.data]);
        this.hasMore.set(page.hasMore);
        this.nextCursor.set(page.nextCursor);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      },
    });
  }

  loadMore(): void {
    this.loadPage(this.filterService.selectedLocationId(), this.nextCursor());
  }

  retry(): void {
    this.samples.set([]);
    this.nextCursor.set(null);
    this.loadPage(this.filterService.selectedLocationId(), null);
  }

  readonly displayColumns = ['id', 'location', 'date', 'depth', 'unitWeight', 'waterContent', 'shearStrength', 'actions'];

  readonly columnLabels = computed(() => UNIT_LABELS[this.unitSystemService.selected()]);

  readonly exceededIds = computed(() => {
    const surpassing = this.statistics()?.samplesSurpassingThreshold;
    return {
      unitWeight:    new Set(surpassing?.unitWeight    ?? []),
      waterContent:  new Set(surpassing?.waterContent  ?? []),
      shearStrength: new Set(surpassing?.shearStrength ?? []),
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

  isAnyExceeded(sample: Sample): boolean {
    const e = this.exceededIds();
    return e.unitWeight.has(sample.id) || e.waterContent.has(sample.id) || e.shearStrength.has(sample.id);
  }

  openAddDialog(): void {
    this.dialog.open(SampleFormDialogComponent, {
      data: { sample: null, locations: this.locations() },
    }).afterClosed().subscribe((result: SampleRequest | undefined) => {
      if (!result) return;
      this.sampleService.create(result).subscribe({
        next: () => {
          this.samples.set([]);
          this.nextCursor.set(null);
          this.loadPage(this.filterService.selectedLocationId(), null);
          this.filterService.bumpMutation();
        },
      });
    });
  }

  openEditDialog(s: Sample): void {
    this.dialog.open(SampleFormDialogComponent, {
      data: { sample: s, locations: this.locations() },
    }).afterClosed().subscribe((result: SampleRequest | undefined) => {
      if (!result) return;
      this.sampleService.update(s.id, result).subscribe({
        next: updated => {
          this.samples.update(prev => prev.map(x => x.id === updated.id ? updated : x));
          this.filterService.bumpMutation();
        },
      });
    });
  }

  openDeleteDialog(s: Sample): void {
    this.dialog.open(DeleteConfirmDialogComponent, { data: s })
      .afterClosed().subscribe((confirmed: boolean) => {
        if (!confirmed) return;
        this.sampleService.remove(s.id).subscribe({
          next: () => {
            this.samples.update(prev => prev.filter(x => x.id !== s.id));
            this.filterService.bumpMutation();
          },
        });
      });
  }
}