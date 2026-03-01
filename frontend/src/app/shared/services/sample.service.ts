import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Sample, SampleCursor, SamplePage, SampleRequest } from '../models/sample.model';

export interface GetPageParams {
  locationId?: string | null;
  cursor?: SampleCursor | null;
  limit?: number;
}

@Injectable({ providedIn: 'root' })
export class SampleService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/api/v1/samples`;

  getPage({ locationId, cursor, limit = 10 }: GetPageParams = {}): Observable<SamplePage> {
    let params = new HttpParams().set('limit', limit);
    if (locationId)        params = params.set('locationId',      locationId);
    if (cursor?.afterTimestamp) params = params.set('afterTimestamp', cursor.afterTimestamp);
    if (cursor?.afterId)   params = params.set('afterId',         cursor.afterId);
    return this.http.get<SamplePage>(`${this.base}/page`, { params });
  }

  create(req: SampleRequest): Observable<Sample> {
    return this.http.post<Sample>(this.base, req);
  }

  update(id: string, req: SampleRequest): Observable<Sample> {
    return this.http.put<Sample>(`${this.base}/${id}`, req);
  }

  remove(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}