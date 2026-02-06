import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CheckIn } from '../models/checkin.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CheckinsService {
  constructor(private http: HttpClient) {}

  create(payload: { idHospede: number; dataEntrada: string; dataSaida: string; adicionalVeiculo: boolean }): Observable<CheckIn> {
    return this.http.post<CheckIn>('/api/checkins', payload);
  }

  update(id: number, payload: { dataEntrada?: string; dataSaida?: string; adicionalVeiculo?: boolean }): Observable<CheckIn> {
    return this.http.put<CheckIn>(`/api/checkins/${id}`, payload);
  }
}
