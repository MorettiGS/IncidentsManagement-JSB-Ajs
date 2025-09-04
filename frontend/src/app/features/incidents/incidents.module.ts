import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

import { IncidentListComponent } from './incident-list/incident-list.component';
import { IncidentDetailComponent } from './incident-detail/incident-detail.component';
import { IncidentFormComponent } from './incident-form/incident-form.component';
import { CommentListComponent } from './comment-list/comment-list.component';
import { CommentFormComponent } from './comment-form/comment-form.component';
import { MaterialModule } from '../../shared/material.module';

const routes: Routes = [
  { path: '', component: IncidentListComponent },
  { path: 'new', component: IncidentFormComponent },
  { path: ':id', component: IncidentDetailComponent }
];

@NgModule({
  declarations: [
    IncidentListComponent,
    IncidentDetailComponent,
    IncidentFormComponent,
    CommentListComponent,
    CommentFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaterialModule,
    RouterModule.forChild(routes)
  ]
})
export class IncidentsModule {}
