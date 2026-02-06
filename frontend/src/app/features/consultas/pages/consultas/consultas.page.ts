import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ConsultasService } from '../../../../core/services/consultas.service';
import { PagedResult } from '../../../../core/models/paged-result.model';
import { GastoHospede } from '../../../../core/models/gasto-hospede.model';

type Modo = 'presentes' | 'ausentes';

@Component({
  standalone: true,
  selector: 'app-consultas-page',
  imports: [CommonModule, FormsModule],
  templateUrl: './consultas.page.html',
  styleUrl: './consultas.page.scss'
})
export class ConsultasPage {
  modo: Modo = 'presentes';
  page = 0;
  size = 10;

  loading = false;
  errorMsg: string | null = null;

  data: PagedResult<GastoHospede> | null = null;

  constructor(private consultas: ConsultasService) {
    this.load();
  }

  changeModo(m: Modo) {
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
    this.errorMsg = null;

    const obs = this.modo === 'presentes'
      ? this.consultas.presentes(this.page, this.size)
      : this.consultas.ausentes(this.page, this.size);

    obs.subscribe({
      next: (res) => { this.data = res; this.loading = false; },
      error: (err) => {
        console.error(err);
        this.errorMsg = 'Erro ao carregar consultas.';
        this.loading = false;
      }
    });
  }

  money(v: number) {
    return (v ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
