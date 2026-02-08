import { Component, DestroyRef, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap, catchError, of, tap, finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { map } from 'rxjs/operators';

import { HospedesService } from '../../../../core/services/hospedes.service';
import { CheckinsService } from '../../../../core/services/checkins.service';
import { AppEventsService } from '../../../../core/services/app-events.service';
import { Guest } from '../../../../core/models/guest.model';
import { getApiErrorMessage } from '../../../../core/utils/api-error-message';

const NumeroLetrasPesquisa = 3;

@Component({
  standalone: true,
  selector: 'app-checkin-panel',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './checkin-panel.component.html',
  styleUrls: ['./checkin-panel.component.scss']
})
export class CheckInPanelComponent {
  guests: Guest[] = [];
  loadingSearch = false;
  saving = false;
  errorMsg: string | null = null;
  successMsg: string | null = null;

  form!: FormGroup;
  
  constructor(
    private fb: FormBuilder,
    private hospedes: HospedesService,
    private checkins: CheckinsService,
    private events: AppEventsService,
    private destroyRef: DestroyRef,
    private zone: NgZone,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      dataEntrada: ['', [Validators.required]],
      dataSaida: ['', [Validators.required]],
      termo: [''],
      idHospede: [null as number | null, [Validators.required]],
      adicionalVeiculo: [false],
    });

    // Busca por termo com loading correto
    this.form.get('termo')!.valueChanges.pipe(
      debounceTime(250),
      map(v => (v ?? '').trim()),
      distinctUntilChanged(),

      tap((term) => {
        this.errorMsg = null;
        this.successMsg = null;

        // sempre limpa seleção quando está digitando
        this.form.get('idHospede')!.setValue(null);

        if (term.length < NumeroLetrasPesquisa) {
          this.loadingSearch = false;
          this.guests = [];
          this.cdr.markForCheck();
          return;
        }

        this.loadingSearch = true;
        this.guests = [];
        this.cdr.markForCheck();
      }),

      switchMap((term) => {
        if (term.length < NumeroLetrasPesquisa) return of([] as Guest[]);

        return this.hospedes.searchByTerm(term).pipe(
          catchError(err => {
            console.error(err);
            this.errorMsg = 'Erro ao buscar hóspedes.';
            return of([] as Guest[]);
          }),
          finalize(() => {
            this.loadingSearch = false;
            this.cdr.markForCheck();
          })
        );
      }),

      takeUntilDestroyed(this.destroyRef)
    ).subscribe(list => {
      this.guests = list;
      this.cdr.markForCheck();
    });

    // Hóspede criado no modal -> pré-seleciona automaticamente
    this.events.guestCreated$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((g) => {
        this.form.get('idHospede')!.setValue(g.id);
        this.form.get('termo')!.setValue(g.nome, { emitEvent: false });
        this.guests = [];
        this.loadingSearch = false;
        this.errorMsg = null;
        this.successMsg = 'Hóspede cadastrado! Você já pode fazer o check-in.';
        setTimeout(() => this.successMsg = null, 2500);
      });
  }

  selectGuest(g: Guest) {
    this.form.get('idHospede')!.setValue(g.id);
    this.form.get('termo')!.setValue(g.nome, { emitEvent: false });
    this.guests = [];
    this.loadingSearch = false;
    this.errorMsg = null;
  }

  pickGuest(ev: MouseEvent, g: Guest) {
    ev.preventDefault();
    ev.stopPropagation();
    this.selectGuest(g);
  }

  submit() {
    this.errorMsg = null;
    this.successMsg = null;

    if (this.form.invalid) {
      this.errorMsg = 'Preencha os campos.';
      return;
    }

    if (this.form.get('dataEntrada')!.invalid || this.form.get('dataSaida')!.invalid) {
      this.errorMsg = 'Data inválida. O ano deve ter 4 dígitos.';
      return;
    }

    const raw = this.form.getRawValue() as any;

    const payload = {
      idHospede: raw.idHospede,
      dataEntrada: raw.dataEntrada,
      dataSaida: raw.dataSaida,
      adicionalVeiculo: !!raw.adicionalVeiculo,
    };

    const entrada = new Date(payload.dataEntrada);
    const saida = new Date(payload.dataSaida);
    if (!(entrada < saida)) {
      this.errorMsg = 'A data/hora de saída deve ser maior que a de entrada.';
      return;
    }

    this.saving = true;

    this.checkins.create(payload).subscribe({
      next: () => {
        this.saving = false;

        this.successMsg = 'Check-in salvo com sucesso!';
        setTimeout(() => this.successMsg = null, 2500);

        // atualiza consultas
        this.events.refreshConsultas();

        // limpa form
        this.form.reset({
          dataEntrada: '',
          dataSaida: '',
          termo: '',
          idHospede: null,
          adicionalVeiculo: false
        });
        this.guests = [];
      },
      error: (err) => {
        console.error(err);
        this.saving = false;
        void this.showApiError(err);
      }
    });
  }
  
  onDateInput(controlName: 'dataEntrada' | 'dataSaida') {
      const ctrl = this.form.get(controlName);
      const v = (ctrl?.value ?? '') as string;
      if (!v) return;

      // v = "202602-02-06T10:30" -> vira "2026-02-06T10:30"
      const m = v.match(/^(\d+)(-.*)$/);
      if (m && m[1].length > 4) {
        const fixed = m[1].slice(0, 4) + m[2];
        ctrl?.setValue(fixed, { emitEvent: false });
      }
    }

  private async showApiError(err: any) {
    const msg = await getApiErrorMessage(err);

    // garante que renderiza imediatamente
    this.zone.run(() => {
      this.errorMsg = msg;
      this.cdr.detectChanges();
    });
  }

}
