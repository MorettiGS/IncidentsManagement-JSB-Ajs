import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Incident } from '../../../core/models/incident.model';
import { Comment } from '../../../core/models/comment.model';
import { IncidentService } from '../../../core/services/incident.service';
import { CommentService } from '../../../core/services/comment.service';

@Component({
  selector: 'app-incident-detail',
  templateUrl: './incident-detail.component.html',
  styleUrls: ['./incident-detail.component.css']
})
export class IncidentDetailComponent implements OnInit {
  incident: Incident | null = null;
  comments: Comment[] = [];
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private incidentService: IncidentService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadIncident(id);
      this.loadComments(id);
    }
  }

  loadIncident(id: string): void {
    this.incidentService.getIncident(id).subscribe({
      next: (incident) => {
        this.incident = incident;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading incident:', error);
        this.loading = false;
      }
    });
  }

  loadComments(incidentId: string): void {
    this.commentService.getCommentsByIncidentId(incidentId).subscribe({
      next: (comments) => {
        this.comments = comments;
      },
      error: (error) => {
        console.error('Error loading comments:', error);
      }
    });
  }

  deleteIncident(): void {
    if (this.incident && confirm('Tem certeza que deseja excluir este incidente?')) {
      this.incidentService.deleteIncident(this.incident.id!).subscribe({
        next: () => {
          this.router.navigate(['/incidents']);
        },
        error: (error) => {
          console.error('Error deleting incident:', error);
        }
      });
    }
  }

  updateStatus(status: string): void {
    if (this.incident) {
      this.incidentService.updateStatus(this.incident.id!, status).subscribe({
        next: (updatedIncident) => {
          this.incident = updatedIncident;
        },
        error: (error) => {
          console.error('Error updating status:', error);
        }
      });
    }
  }
}
