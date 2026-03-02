import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '../auth/auth.service';
import { GraphComponent } from '../graph/graph';
import { SampleTableComponent } from '../samples/sample-table/sample-table';
import { StatisticsComponent } from '../statistics/statistics';

@Component({
  selector: 'app-home',
  imports: [
    MatButtonModule, MatIconModule, MatListModule,
    MatSidenavModule, MatToolbarModule, MatTooltipModule,
    GraphComponent,
    SampleTableComponent,
    StatisticsComponent,
  ],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent {
  private readonly authService = inject(AuthService);
  private readonly router      = inject(Router);

  readonly activeView = signal<'samples' | 'graph'>('samples');

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}