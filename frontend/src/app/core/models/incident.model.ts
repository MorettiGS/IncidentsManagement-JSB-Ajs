export interface Incident {
  id?: string;
  titulo: string;
  descricao?: string;
  prioridade: 'BAIXA' | 'MEDIA' | 'ALTA';
  status: 'ABERTA' | 'EM_ANDAMENTO' | 'RESOLVIDA' | 'CANCELADA';
  responsavelEmail: string;
  tags: string[];
  dataAbertura?: string;
  dataAtualizacao?: string;
}

export interface IncidentFilters {
  status?: string;
  prioridade?: string;
  search?: string;
  page?: number;
  size?: number;
  sort?: string;
}

export interface IncidentStats {
  byStatus: { [key: string]: number };
  byPriority: { [key: string]: number };
}
