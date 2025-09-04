import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { IncidentStats } from '../models/incident.model';

@Injectable({
  providedIn: 'root'
})
export class StatsService {
  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  getIncidentStats(): Observable<IncidentStats> {
    return this.http.get<IncidentStats>(`${this.baseUrl}/stats/incidents`);
  }
}
