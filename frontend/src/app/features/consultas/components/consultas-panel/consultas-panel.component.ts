import { Component } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ConsultasService } from '../../../../core/services/consultas.service';
import { GastoHospede } from '../../../../core/models/gasto-hospede.model';
import { PagedResult } from '../../../../core/models/paged-result.model';

type Modo = 'presentes' | 'ausentes';

@Component({
  standalone: true,
  selector: 'app-consultas-panel',
  imports: [CommonModule, FormsModule, CurrencyPipe],
  templateUrl: './consultas-panel.component.html',
  styleUrls: ['./consultas-panel.component.scss']
})
export class ConsultasPanelComponent {
  modo: Modo = 'presentes';
  page = 0;
  size = 10;

  loading = false;
  data: PagedResult<GastoHospede> | null = null;

  constructor(private consultas: ConsultasService) {
    this.load();
  }

  setModo(m: Modo) {
    this.modo = m;
    this.page = 0;
    this.load();
  }

  prev() {
    if (!this.data?.hasPrevious) return;
    this.page--;
    this.load();
  }

  next() {
    if (!this.data?.hasNext) return;
    this.page++;
    this.load();
  }

  load() {
    this.loading = true;
    const obs = this.modo === 'presentes'
      ? this.consultas.presentes(this.page, this.size)
      : this.consultas.ausentes(this.page, this.size);

    obs.subscribe({
      next: (res) => { this.data = res; this.loading = false; },
      error: (err) => { console.error(err); this.loading = false; this.data = { items: [], page: 0, size: this.size, totalElements: 0, totalPages: 0, hasNext: false, hasPrevious: false }; }
    });
  }
}
