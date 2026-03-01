import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '../auth/auth.service';
import { MOCK_LOCATIONS, MOCK_SAMPLES } from '../shared/mock-data';
import { StatisticsComponent } from '../statistics/statistics';
import { SampleTableComponent } from '../samples/sample-table/sample-table';

@Component({
  selector: 'app-home',
  imports: [
    MatToolbarModule, MatButtonModule,
    MatIconModule, MatTooltipModule,
    StatisticsComponent,
    SampleTableComponent,
  ],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly locations = MOCK_LOCATIONS;
  readonly selectedLocationId = signal<string | null>(null);

  readonly displayedSamples = computed(() => {
    const locId = this.selectedLocationId();
    return locId ? MOCK_SAMPLES.filter(s => s.locationId === locId) : [...MOCK_SAMPLES];
  });

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}