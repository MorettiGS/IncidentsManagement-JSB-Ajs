import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Incident } from '../../../core/models/incident.model';
import { IncidentService } from '../../../core/services/incident.service';

@Component({
  selector: 'app-incident-form',
  templateUrl: './incident-form.component.html',
  styleUrls: ['./incident-form.component.css']
})
export class IncidentFormComponent implements OnInit {
  incidentForm: FormGroup;
  isEditMode = false;
  incidentId?: string;

  priorities = [
    { value: 'BAIXA', label: 'Baixa' },
    { value: 'MEDIA', label: 'Média' },
    { value: 'ALTA', label: 'Alta' }
  ];

  statuses = [
    { value: 'ABERTA', label: 'Aberta' },
    { value: 'EM_ANDAMENTO', label: 'Em Andamento' },
    { value: 'RESOLVIDA', label: 'Resolvida' },
    { value: 'CANCELADA', label: 'Cancelada' }
  ];

  constructor(
    private fb: FormBuilder,
    private incidentService: IncidentService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.incidentForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(120)]],
      descricao: ['', Validators.maxLength(5000)],
      prioridade: ['', Validators.required],
      status: ['', Validators.required],
      responsavelEmail: ['', [Validators.required, Validators.email]],
      tags: ['']
    });
  }

  ngOnInit(): void {
    this.incidentId = this.route.snapshot.params['id'];
    this.isEditMode = !!this.incidentId;

    if (this.isEditMode) {
      this.loadIncident();
    }
  }

  loadIncident(): void {
    this.incidentService.getIncident(this.incidentId!).subscribe({
      next: (incident) => {
        this.incidentForm.patchValue({
          ...incident,
          tags: incident.tags.join(', ')
        });
      },
      error: (error) => {
        console.error('Error loading incident:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.incidentForm.valid) {
      const formValue = this.incidentForm.value;
      const tagsArr = (formValue.tags || '')
        .split(',')
        .map((t: string) => t.trim())
        .filter((t: string) => t);

      const incident: Incident = {
        ...formValue,
        tags: tagsArr
      };

      const operation = this.isEditMode
        ? this.incidentService.updateIncident(this.incidentId!, incident)
        : this.incidentService.createIncident(incident);

      operation.subscribe({
        next: () => {
          this.router.navigate(['/incidents']);
        },
        error: (error) => {
          console.error('Error saving incident:', error);
        }
      });
    }
  }

  get titulo() { return this.incidentForm.get('titulo'); }
  get descricao() { return this.incidentForm.get('descricao'); }
  get prioridade() { return this.incidentForm.get('prioridade'); }
  get status() { return this.incidentForm.get('status'); }
  get responsavelEmail() { return this.incidentForm.get('responsavelEmail'); }
}
