import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Incident {
  id?: string;
  titulo: string;
  descricao?: string;
  prioridade: 'BAIXA' | 'MEDIA' | 'ALTA';
  status: 'ABERTA' | 'EM_ANDAMENTO' | 'RESOLVIDA' | 'CANCELADA';
  responsavelEmail: string;
  tags: string[];
  dataAbertura?: string;
  dataAtualizacao?: string;
}

export interface IncidentFilters {
  status?: string;
  prioridade?: string;
  search?: string;
  page?: number;
  size?: number;
  sort?: string;
}

@Injectable({
  providedIn: 'root'
})
export class IncidentService {
  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  getIncidents(filters: IncidentFilters = {}): Observable<any> {
    let params = new HttpParams();
    
    Object.keys(filters).forEach(key => {
      const value = filters[key as keyof IncidentFilters];
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get(`${this.baseUrl}/incidents`, { params });
  }

  getIncident(id: string): Observable<Incident> {
    return this.http.get<Incident>(`${this.baseUrl}/incidents/${id}`);
  }

  createIncident(incident: Incident): Observable<Incident> {
    return this.http.post<Incident>(`${this.baseUrl}/incidents`, incident);
  }

  updateIncident(id: string, incident: Incident): Observable<Incident> {
    return this.http.put<Incident>(`${this.baseUrl}/incidents/${id}`, incident);
  }

  deleteIncident(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/incidents/${id}`);
  }

  updateStatus(id: string, status: string): Observable<Incident> {
    return this.http.patch<Incident>(`${this.baseUrl}/incidents/${id}/status`, { status });
  }
}
