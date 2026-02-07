import { Component, DestroyRef, ChangeDetectorRef } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Subscription } from 'rxjs';

import { ConsultasService } from '../../../../core/services/consultas.service';
import { AppEventsService } from '../../../../core/services/app-events.service';
import { GastoHospede } from '../../../../core/models/gasto-hospede.model';
import { PagedResult } from '../../../../core/models/paged-result.model';

type Modo = 'presentes' | 'ausentes';

@Component({
  standalone: true,
  selector: 'app-consultas-panel',
  imports: [CommonModule, CurrencyPipe, DatePipe],
  templateUrl: './consultas-panel.component.html',
  styleUrls: ['./consultas-panel.component.scss']
})
export class ConsultasPanelComponent {
  modo: Modo = 'presentes';
  page = 0;
  size = 10;
  sizeOptions = [20, 10, 5, 3];
  
  // radio group único
  radioName = 'consultasModo_' + Math.random().toString(36).slice(2);

  loading = false;
  data: PagedResult<GastoHospede> | null = null;

  searchedOnce = false;

  private loadSub?: Subscription;
  private reqToken = 0;

  constructor(
    private consultas: ConsultasService,
    private events: AppEventsService,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {
    this.destroyRef.onDestroy(() => this.loadSub?.unsubscribe());

    // se salvar check-in, você pode optar por recarregar automaticamente a página atual:
    // aqui deixei manual (não mexe na grid sozinho)
    this.events.refreshConsultas$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        // voltar pra primeira página pra facilitar ver o novo registro
        this.page = 0;
        this.load();
      });

      this.load(); // carrega a listagem padrão ao abrir (presentes)
  }

  setSize(n: number) {
    if (!n || this.size === n) return;
    this.size = n;
    this.page = 0;
    this.load();
    this.cdr.markForCheck()
  }

  setModo(m: Modo) {
    if (this.modo === m) return;
    this.modo = m;

    // troca de filtro exige uma nova pesquisa (1 clique no Pesquisar)
    this.page = 0;
    this.searchedOnce = false;
    this.data = null;

    // cancela request pendente (evita “troca sozinho”)
    this.loading = false;     // <- importante se estava carregando
    this.loadSub?.unsubscribe();
    this.reqToken++;

    this.cdr.markForCheck();  // <- força atualizar a tela
    this.load();
  }

  prev() {
    if (!this.searchedOnce || this.loading) return;
    if (this.page <= 0) return;
    this.page--;
    this.load();
  }

  next() {
    if (!this.searchedOnce || this.loading) return;
    if (this.data && !this.data.hasNext) return;
    this.page++;
    this.load();
  }

  private load() {

    this.loadSub?.unsubscribe();

    const token = ++this.reqToken;
    const modoSnap = this.modo;
    const pageSnap = this.page;
    const sizeSnap = this.size;

    this.loading = true;
    this.cdr.markForCheck(); // <- faz aparecer "Carregando..." imediatamente

    const obs = modoSnap === 'presentes'
      ? this.consultas.presentes(pageSnap, sizeSnap)
      : this.consultas.ausentes(pageSnap, sizeSnap);

    this.loadSub = obs.subscribe({
      next: (res) => {

        if (token !== this.reqToken) return;
        if (modoSnap !== this.modo || pageSnap !== this.page) return;

        this.data = res;
        this.loading = false;
        this.searchedOnce = true;
        this.cdr.markForCheck(); // <- faz a grid renderizar os dados na hora
      },
      error: (err) => {
        if (token !== this.reqToken) return;

        console.error(err);
        this.loading = false;

        // mantém searchedOnce como está (se já tinha dados, mantém navegação)
        this.data = {
          items: [],
          page: 0,
          size: this.size,
          totalElements: 0,
          totalPages: 0,
          hasNext: false,
          hasPrevious: false
        };
        this.cdr.markForCheck(); // <- atualiza a grid/estado na hora
      }
    });
  }
}
