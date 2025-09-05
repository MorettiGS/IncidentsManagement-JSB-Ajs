import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Comment } from '../../../core/models/comment.model';
import { CommentService } from '../../../core/services/comment.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent implements OnInit, OnChanges {
  @Input() comments: Comment[] = [];
  @Input() incidentId: string | undefined;

  loading = false;
  error = '';

  constructor(
    private commentService: CommentService,
  ) {}

  ngOnInit(): void {
    if (this.incidentId) {
      this.loadComments();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['incidentId'] && !changes['incidentId'].isFirstChange()) {
      this.loadComments();
    }

    if (changes['comments'] && changes['comments'].currentValue) {
      this.comments = changes['comments'].currentValue;
    }
  }

  loadComments(page = 0, size = 100): void {
    if (!this.incidentId) {
      this.comments = [];
      return;
    }

    this.loading = true;
    this.error = '';

    this.commentService.getCommentsByIncidentId(this.incidentId, page, size).pipe(
      finalize(() => this.loading = false)
    ).subscribe({
      next: (res: any) => {
        if (res && Array.isArray(res.content)) {
          this.comments = res.content;
        } else if (Array.isArray(res)) {
          this.comments = res;
        } else {
          this.comments = (res && res.content) ? res.content : [];
        }
      },
      error: (err) => {
        console.error('Failed to load comments', err);
        this.error = 'Failed to load comments';
      }
    });
  }

  onCommentCreated() {
    this.loadComments();
  }
}
