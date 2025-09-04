import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Comment } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  getCommentsByIncidentId(incidentId: string, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get(`${this.baseUrl}/incidents/${incidentId}/comments`, { params });
  }

  addComment(incidentId: string, comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(`${this.baseUrl}/incidents/${incidentId}/comments`, comment);
  }
}
