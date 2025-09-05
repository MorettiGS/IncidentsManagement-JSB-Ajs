import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UserProfile {
  id?: string;
  email: string;
  name?: string;
  roles?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private base = `${environment.apiBaseUrl}/auth`;

  constructor(private http: HttpClient) {}

  getMe(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.base}/me`);
  }

  updateMe(payload: Partial<UserProfile>) {
    return this.http.put<UserProfile>(`${this.base}/me`, payload);
  }
}
