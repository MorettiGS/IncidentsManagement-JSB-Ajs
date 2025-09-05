import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentService } from '../../../core/services/comment.service';
import { Comment } from '../../../core/models/comment.model';

@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.css']
})
export class CommentFormComponent {
  @Input() incidentId: string | undefined;
  @Output() commentCreated = new EventEmitter<Comment>();
  commentForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private commentService: CommentService
  ) {
    this.commentForm = this.fb.group({
      autor: ['', Validators.required],
      mensagem: ['', [Validators.required, Validators.maxLength(2000)]]
    });
  }

  onSubmit(): void {
    if (this.commentForm.valid && this.incidentId) {
      const comment: Comment = {
        ...this.commentForm.value,
        incidentId: this.incidentId
      };

      this.commentService.addComment(this.incidentId, comment).subscribe({
        next: (newComment) => {
          this.commentCreated.emit(newComment);
          this.commentForm.reset();
        },
        error: (error) => {
          console.error('Error adding comment:', error);
        }
      });
    }
  }
}
