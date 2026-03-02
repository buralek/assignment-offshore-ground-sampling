import { Component, inject, signal } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { Location } from '../../models/location.model';
import { FilterService } from '../../services/filter.service';
import { LocationService } from '../../services/location.service';

@Component({
  selector: 'app-location-filter',
  imports: [MatFormFieldModule, MatSelectModule],
  templateUrl: './location-filter.html',
  styleUrl: './location-filter.css',
})
export class LocationFilterComponent {
  private readonly locationService = inject(LocationService);
  readonly filterService           = inject(FilterService);

  readonly locations = signal<Location[]>([]);

  constructor() {
    this.locationService.getAll().subscribe(locs => this.locations.set(locs));
  }
}