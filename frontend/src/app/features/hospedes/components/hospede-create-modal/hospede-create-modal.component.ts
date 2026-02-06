import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { HospedesService } from '../../../../core/services/hospedes.service';

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

  constructor(private fb: FormBuilder, private hospedes: HospedesService) {
    this.form = this.fb.group({
      nome: ['', [Validators.required]],
      documento: ['', [Validators.required]],
      telefone: ['', [Validators.required]],
    });
  }

  close() {
    this.errorMsg = null;
    this.closed.emit();
  }

  submit() {
    if (this.form.invalid) {
      this.errorMsg = 'Preencha os campos.';
      return;
    }
    this.loading = true;
    this.errorMsg = null;

    this.hospedes.create(this.form.getRawValue() as any).subscribe({
      next: () => {
        this.loading = false;
        this.form.reset();
        this.close();
      },
      error: (err) => {
        console.error(err);
        this.errorMsg = err?.error?.message ?? 'Erro ao salvar hóspede.';
        this.loading = false;
      }
    });
  }
}
