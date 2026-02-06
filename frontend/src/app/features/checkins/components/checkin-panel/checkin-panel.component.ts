import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap, catchError, of, tap } from 'rxjs';

import { HospedesService } from '../../../../core/services/hospedes.service';
import { CheckinsService } from '../../../../core/services/checkins.service';
import { Guest } from '../../../../core/models/guest.model';

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

  form;

  constructor(
    private fb: FormBuilder,
    private hospedes: HospedesService,
    private checkins: CheckinsService
  ) {
    this.form = this.fb.group({
      dataEntrada: ['', [Validators.required]],
      dataSaida: ['', [Validators.required]],
      termo: [''],
      idHospede: [null as number | null, [Validators.required]],
      adicionalVeiculo: [false],
    });

    this.form.controls.termo.valueChanges.pipe(
      debounceTime(250),
      distinctUntilChanged(),
      tap(() => {
        this.errorMsg = null;
        this.loadingSearch = true;
        this.guests = [];
        this.form.controls.idHospede.setValue(null);
      }),
      filter(v => (v ?? '').trim().length >= 2),
      switchMap(v => this.hospedes.searchByTerm(v ?? '').pipe(
        catchError(err => { console.error(err); this.errorMsg = 'Erro ao buscar hóspedes.'; return of([]); })
      )),
      tap(() => this.loadingSearch = false)
    ).subscribe(list => this.guests = list);
  }

  selectGuest(g: Guest) {
    this.form.controls.idHospede.setValue(g.id);
    this.form.controls.termo.setValue(g.nome, { emitEvent: false });
    this.guests = [];
  }

  submit() {
    if (this.form.invalid) {
      this.errorMsg = 'Preencha os campos.';
      return;
    }

    const raw = this.form.getRawValue();
    const payload = {
      idHospede: raw.idHospede!,
      dataEntrada: this.toIsoLocal(raw.dataEntrada!),
      dataSaida: this.toIsoLocal(raw.dataSaida!),
      adicionalVeiculo: !!raw.adicionalVeiculo,
    };

    this.saving = true;
    this.errorMsg = null;

    this.checkins.create(payload).subscribe({
      next: () => { this.saving = false; this.form.patchValue({ termo: '', idHospede: null }); },
      error: (err) => {
        console.error(err);
        this.errorMsg = err?.error?.message ?? 'Erro ao salvar check-in.';
        this.saving = false;
      }
    });
  }

  private toIsoLocal(v: string): string {
    return v && v.length === 16 ? `${v}:00` : v;
  }
}
