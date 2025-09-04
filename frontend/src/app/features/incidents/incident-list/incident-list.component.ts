import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import { Incident, IncidentFilters } from '../../../core/models/incident.model';
import { IncidentService } from '../../../core/services/incident.service';
import { StatsService } from '../../../core/services/stats.service';

@Component({
  selector: 'app-incident-list',
  templateUrl: './incident-list.component.html',
  styleUrls: ['./incident-list.component.css']
})
export class IncidentListComponent implements OnInit, AfterViewInit {
  incidents: Incident[] = [];
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  sortField = 'dataAbertura';
  sortDirection: 'asc' | 'desc' | '' = 'desc';

  filtersForm: FormGroup;
  stats: any = {};

  displayedColumns: string[] = ['titulo', 'prioridade', 'status', 'responsavelEmail', 'dataAbertura', 'actions'];
  dataSource = new MatTableDataSource<Incident>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private incidentService: IncidentService,
    private statsService: StatsService,
    private fb: FormBuilder
  ) {
    this.filtersForm = this.fb.group({
      status: [''],
      prioridade: [''],
      search: ['']
    });
  }

  ngOnInit(): void {
    this.loadIncidents();
    this.loadStats();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadIncidents(): void {
    const filters: IncidentFilters = {
      ...this.filtersForm.value,
      page: this.pageIndex,
      size: this.pageSize,
      sort: `${this.sortField},${this.sortDirection || 'desc'}`
    };

    this.incidentService.getIncidents(filters).subscribe({
      next: (response: any) => {
        this.incidents = response.content || [];
        this.totalElements = response.totalElements ?? (this.incidents.length || 0);

		this.dataSource.data = this.incidents;
      },
      error: (error) => {
        console.error('Error loading incidents:', error);
      }
    });
  }

  loadStats(): void {
    this.statsService.getIncidentStats().subscribe({
      next: (stats) => {
        this.stats = stats;
      },
      error: (error) => {
        console.error('Error loading stats:', error);
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadIncidents();
  }

  onSortChange(sort: Sort): void {
    if (!sort.direction) {
      this.sortField = 'dataAbertura';
      this.sortDirection = 'desc';
    } else {
      this.sortField = sort.active;
      this.sortDirection = sort.direction as 'asc' | 'desc';
    }
    this.loadIncidents();
  }

  onFilterChange(): void {
    this.pageIndex = 0;
    this.loadIncidents();
  }

  deleteIncident(id: string): void {
    if (confirm('Tem certeza que deseja excluir este incidente?')) {
      this.incidentService.deleteIncident(id).subscribe({
        next: () => {
          this.loadIncidents();
          this.loadStats();
        },
        error: (error) => {
          console.error('Error deleting incident:', error);
        }
      });
    }
  }
}
