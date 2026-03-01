import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SampleStatistics } from '../models/statistics.model';

@Injectable({ providedIn: 'root' })
export class StatisticsService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/api/v1/statistics/samples`;

  get(locationId?: string | null): Observable<SampleStatistics> {
    const params = locationId
      ? new HttpParams().set('locationId', locationId)
      : new HttpParams();
    return this.http.get<SampleStatistics>(this.base, { params });
  }
}