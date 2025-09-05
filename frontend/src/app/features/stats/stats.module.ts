import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatsComponent } from './stats.component';
import { StatsRoutingModule } from './stats-routing.module';
import { MaterialModule } from '../../shared/material.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  declarations: [StatsComponent],
  imports: [
    CommonModule,
    MaterialModule,
    StatsRoutingModule,
    MatProgressSpinnerModule,
    MatListModule,
    MatCardModule
  ]
})
export class StatsModule {}
