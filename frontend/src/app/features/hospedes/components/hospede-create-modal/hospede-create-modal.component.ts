import { Component, EventEmitter, Input, Output, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { HospedesService } from '../../../../core/services/hospedes.service';
import { AppEventsService } from '../../../../core/services/app-events.service';
import { Guest } from '../../../../core/models/guest.model';
import { getApiErrorMessage } from '../../../../core/utils/api-error-message';

@Component({
  standalone: true,
  selector: 'app-hospede-create-modal',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './hospede-create-modal.component.html',
  styleUrls: ['./hospede-create-modal.component.scss']
})
export class HospedeCreateModalComponent {
  @Input() open = false;
  @Output() closed = new EventEmitter<void>();

  loading = false;
  errorMsg: string | null = null;

  form;

  constructor(
    private fb: FormBuilder,
    private hospedes: HospedesService,
    private events: AppEventsService,
    private zone: NgZone,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required]],
      documento: ['', [Validators.required, this.docValidator]],
      telefone: ['', [Validators.required, this.phoneValidator]],
    });

    // Máscara documento
    this.form.get('documento')!.valueChanges.subscribe(v => {
      const digits = this.onlyDigits(v ?? '');
      const masked = this.formatCpfCnpj(digits);
      if ((v ?? '') !== masked) {
        this.form.patchValue({ documento: masked }, { emitEvent: false });
      }
    });

    // Máscara telefone
    this.form.get('telefone')!.valueChanges.subscribe(v => {
      const digits = this.onlyDigits(v ?? '');
      const masked = this.formatPhone(digits);
      if ((v ?? '') !== masked) {
        this.form.patchValue({ telefone: masked }, { emitEvent: false });
      }
    });
  }

  close() {
    this.loading = false;
    this.errorMsg = null;
    this.closed.emit();
  }

  submit() {
    // Evita duplo clique
    if (this.loading) return;

    if (this.form.invalid) {
      this.errorMsg = 'Preencha os campos corretamente.';
      return;
    }

    this.loading = true;
    this.errorMsg = null;

    const raw = this.form.getRawValue() as any;

    const payload = {
      nome: raw.nome,
      documento: this.onlyDigits(raw.documento),
      telefone: this.onlyDigits(raw.telefone),
    };

    this.hospedes.create(payload).subscribe({
      next: (res: Guest) => {
        this.loading = false;
        this.form.reset();
        this.events.guestCreated(res);
        this.close();
      },
      error: (err) => {
        console.error(err);
        void this.showApiError(err);
      }
    });
  }

  private async showApiError(err: any) {
    const msg = await getApiErrorMessage(err);

    // garante que renderiza imediatamente
    this.zone.run(() => {
      this.loading = false;
      this.errorMsg = msg;
      this.cdr.detectChanges();
    });
  }

  // ====== VALIDATORS ======
  private docValidator = (c: AbstractControl): ValidationErrors | null => {
    const digits = this.onlyDigits(c.value ?? '');
    // CPF 11 ou CNPJ 14
    return (digits.length === 11 || digits.length === 14) ? null : { docInvalido: true };
  };

  private phoneValidator = (c: AbstractControl): ValidationErrors | null => {
    const digits = this.onlyDigits(c.value ?? '');
    // telefone 10 ou 11 dígitos (com DDD)
    return (digits.length === 10 || digits.length === 11) ? null : { telefoneInvalido: true };
  };

  // ====== MASK HELPERS ======
  private onlyDigits(v: string): string {
    return v.replace(/\D/g, '');
  }

  private formatCpfCnpj(d: string): string {
    if (d.length <= 11) {
      // CPF: 000.000.000-00
      d = d.slice(0, 11);
      d = d.replace(/^(\d{3})(\d)/, '$1.$2');
      d = d.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
      d = d.replace(/^(\d{3})\.(\d{3})\.(\d{3})(\d{1,2})$/, '$1.$2.$3-$4');
      return d;
    }
    // CNPJ: 00.000.000/0000-00
    d = d.slice(0, 14);
    d = d.replace(/^(\d{2})(\d)/, '$1.$2');
    d = d.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
    d = d.replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3/$4');
    d = d.replace(/^(\d{2})\.(\d{3})\.(\d{3})\/(\d{4})(\d{1,2})$/, '$1.$2.$3/$4-$5');
    return d;
  }

  private formatPhone(d: string): string {
    d = d.slice(0, 11);

    if (d.length <= 10) {
      // (00) 0000-0000
      d = d.replace(/^(\d{2})(\d)/, '($1) $2');
      d = d.replace(/^\((\d{2})\)\s(\d{4})(\d{1,4})$/, '($1) $2-$3');
      return d;
    }

    // (00) 00000-0000
    d = d.replace(/^(\d{2})(\d)/, '($1) $2');
    d = d.replace(/^\((\d{2})\)\s(\d{5})(\d{1,4})$/, '($1) $2-$3');
    return d;
  }
}
