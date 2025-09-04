import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IncidentListComponent } from './incident-list/incident-list.component';
import { IncidentDetailComponent } from './incident-detail/incident-detail.component';
import { IncidentFormComponent } from './incident-form/incident-form.component';
import { AuthGuard } from '../../core/guards/auth.guard';

const routes: Routes = [
  { path: '', component: IncidentListComponent, canActivate: [AuthGuard] },
  { path: 'new', component: IncidentFormComponent, canActivate: [AuthGuard] },
  { path: ':id', component: IncidentDetailComponent, canActivate: [AuthGuard] },
  { path: ':id/edit', component: IncidentFormComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IncidentsRoutingModule { }
