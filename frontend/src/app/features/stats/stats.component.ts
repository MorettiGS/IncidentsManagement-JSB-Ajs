import { Component, OnInit } from '@angular/core';
import { StatsService } from '../../core/services/stats.service';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {
  loading = false;
  error: string | null = null;
  stats: { byStatus: Record<string, number>, byPriority: Record<string, number> } = { byStatus: {}, byPriority: {} };
  total = 0;

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.statsService.getIncidentStats().subscribe({
      next: (res: any) => {
        try {
          const rawStatus = res?.byStatus ?? res?.status ?? res?.by_status ?? {};
          const rawPriority = res?.byPriority ?? res?.priority ?? res?.by_priority ?? {};

          const byStatus = this.toNumberMap(rawStatus);
          const byPriority = this.toNumberMap(rawPriority);

          this.stats = { byStatus, byPriority };

          this.total = Object.values(byStatus).reduce((s, v) => s + v, 0);
        } catch (e: any) {
          console.error('Stats parsing error', e);
          this.error = 'Erro ao processar estatísticas';
        } finally {
          this.loading = false;
        }
      },
      error: (err: any) => {
        console.error('Failed to load stats', err);
        this.error = err?.error?.message ?? 'Falha ao carregar estatísticas';
        this.loading = false;
      }
    });
  }

  private toNumberMap(raw: any): Record<string, number> {
    if (!raw || typeof raw !== 'object') return {};
    const out: Record<string, number> = {};
    Object.keys(raw).forEach(k => {
      const v = raw[k];
      let n = 0;
      if (v == null) n = 0;
      else if (typeof v === 'number') n = v;
      else {
        const parsed = Number(String(v));
        n = Number.isFinite(parsed) ? parsed : 0;
      }
      out[k] = n;
    });
    return out;
  }

  sortedEntries(map: Record<string, number>): [string, number][] {
    return Object.entries(map || {}).sort((a, b) => b[1] - a[1]);
  }

  entries(map: Record<string, number>) {
    return this.sortedEntries(map);
  }
}
