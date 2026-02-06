import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { HospedesService } from '../../../../core/services/hospedes.service';

@Component({
  standalone: true,
  selector: 'app-hospede-create-page',
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h1>Cadastrar hóspede</h1>

    <form [formGroup]="form" (ngSubmit)="submit()">
      <label>Nome</label>
      <input formControlName="nome" />

      <label>Documento</label>
      <input formControlName="documento" />

      <label>Telefone</label>
      <input formControlName="telefone" />

      <button type="submit" [disabled]="form.invalid || loading">Salvar</button>
    </form>

    <pre *ngIf="result">{{ result | json }}</pre>
  `
})
export class HospedeCreatePage {
  loading = false;
  result: any = null;

  form;

  constructor(private fb: FormBuilder, private hospedes: HospedesService) {
    this.form = this.fb.group({
      nome: ['', [Validators.required]],
      documento: ['', [Validators.required]],
      telefone: ['', [Validators.required]],
    });
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;

    this.hospedes.create(this.form.getRawValue() as any).subscribe({
      next: (res) => { this.result = res; this.loading = false; },
      error: (err) => { console.error(err); this.loading = false; }
    });
  }
}
