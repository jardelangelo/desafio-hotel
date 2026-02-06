import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap, catchError, of, tap } from 'rxjs';

import { HospedesService } from '../../../../core/services/hospedes.service';
import { CheckinsService } from '../../../../core/services/checkins.service';
import { Guest } from '../../../../core/models/guest.model';

@Component({
  standalone: true,
  selector: 'app-checkin-create-page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './checkin-create.page.html',
  styleUrls: ['./checkin-create.page.scss']
})
export class CheckInCreatePage {
  guests: Guest[] = [];
  selectedGuest: Guest | null = null;

  loadingSearch = false;
  loadingSubmit = false;
  errorMsg: string | null = null;
  result: any = null;

  form;

  constructor(
    private fb: FormBuilder,
    private hospedes: HospedesService,
    private checkins: CheckinsService
  ) {
    this.form = this.fb.group({
      termo: [''],
      idHospede: [null as number | null, [Validators.required]],
      dataEntrada: ['', [Validators.required]],
      dataSaida: ['', [Validators.required]],
      adicionalVeiculo: [false]
    });

    this.form.controls.termo.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => {
        this.errorMsg = null;
        this.loadingSearch = true;
        this.guests = [];
        this.selectedGuest = null;
        this.form.controls.idHospede.setValue(null);
      }),
      filter(v => (v ?? '').trim().length >= 2),
      switchMap(v =>
        this.hospedes.searchByTerm(v ?? '').pipe(
          catchError(err => {
            console.error(err);
            this.errorMsg = 'Erro ao buscar hóspedes.';
            return of([]);
          })
        )
      ),
      tap(() => this.loadingSearch = false),
    ).subscribe(list => this.guests = list);
  }

  selectGuest(g: Guest) {
    this.selectedGuest = g;
    this.form.controls.idHospede.setValue(g.id);
    this.guests = [];
    this.form.controls.termo.setValue(`${g.nome} (${g.documento})`, { emitEvent: false });
  }

  submit() {
    this.errorMsg = null;
    this.result = null;

    if (this.form.invalid) {
      this.errorMsg = 'Preencha todos os campos obrigatórios.';
      return;
    }

    const raw = this.form.getRawValue();
    const payload = {
      idHospede: raw.idHospede!,
      dataEntrada: this.toIsoLocal(raw.dataEntrada!),
      dataSaida: this.toIsoLocal(raw.dataSaida!),
      adicionalVeiculo: !!raw.adicionalVeiculo
    };

    this.loadingSubmit = true;
    this.checkins.create(payload).subscribe({
      next: (res) => { this.result = res; this.loadingSubmit = false; },
      error: (err) => {
        console.error(err);
        this.errorMsg = err?.error?.message ?? 'Erro ao realizar check-in.';
        this.loadingSubmit = false;
      }
    });
  }

  private toIsoLocal(v: string): string {
    return v && v.length === 16 ? `${v}:00` : v;
  }
}
