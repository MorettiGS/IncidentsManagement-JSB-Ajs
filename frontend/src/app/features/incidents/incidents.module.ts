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
import { IncidentsRoutingModule } from './incidents-routing.module';

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
    IncidentsRoutingModule
  ]
})
export class IncidentsModule {}
