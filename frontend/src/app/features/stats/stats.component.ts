import { Component, OnInit } from '@angular/core';
import { StatsService } from '../../core/services/stats.service';
import { IncidentStats } from '../../core/models/incident.model';
import { timeout, catchError, of } from 'rxjs';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {
  stats: IncidentStats | null = null;
  loading = true;
  error = '';

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.loading = true;
    this.error = '';
    this.statsService.getIncidentStats().pipe(
      timeout(5000),
      catchError((err) => {
        console.error('Stats load error', err);
        this.error = 'Falha ao carregar estatísticas. Tente novamente mais tarde.';
        this.loading = false;
        return of(null);
      })
    ).subscribe((res) => {
      this.stats = res;
      this.loading = false;
    });
  }

  get total(): number {
    if (!this.stats) return 0;
    return Object.values(this.stats.byStatus || {}).reduce((a, b) => a + b, 0);
  }

  entries(obj: Record<string, number> | undefined): Array<[string, number]> {
    return Object.entries(obj ?? {});
  }
}
