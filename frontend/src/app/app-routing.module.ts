import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/incidents', pathMatch: 'full' },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  { 
    path: 'incidents', 
    loadChildren: () => import('./features/incidents/incidents.module').then(m => m.IncidentsModule),
    // canActivate: [AuthGuard]
  },
  {
    path: 'stats',
    loadChildren: () => import('./features/stats/stats.module').then(m => m.StatsModule),
    // canActivate: [AuthGuard]
  },
  { path: '**', redirectTo: '/incidents' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
