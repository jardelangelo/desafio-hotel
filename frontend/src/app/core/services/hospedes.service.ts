import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Guest } from '../models/guest.model';

@Injectable({ providedIn: 'root' })
export class HospedesService {
  private readonly baseUrl = '/api/hospedes';
  private readonly searchUrl = `${this.baseUrl}/buscar`;

  constructor(private http: HttpClient) {}

  create(payload: { nome: string; documento: string; telefone: string }): Observable<Guest> {
    return this.http.post<Guest>(this.baseUrl, payload);
  }

  searchByTerm(termo: string) {
    const t = encodeURIComponent(termo.trim());
    return this.http.get<Guest[]>(`/api/hospedes/buscar?termo=${t}`);
  }
}