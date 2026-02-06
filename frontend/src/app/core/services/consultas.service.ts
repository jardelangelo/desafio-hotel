import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagedResult } from '../models/paged-result.model';
import { GastoHospede } from '../models/gasto-hospede.model';

@Injectable({ providedIn: 'root' })
export class ConsultasService {
  constructor(private http: HttpClient) {}

  presentes(page = 0, size = 10): Observable<PagedResult<GastoHospede>> {
    return this.http.get<PagedResult<GastoHospede>>(`/api/checkins/presentes?page=${page}&size=${size}`);
  }

  ausentes(page = 0, size = 10): Observable<PagedResult<GastoHospede>> {
    return this.http.get<PagedResult<GastoHospede>>(`/api/checkins/ausentes?page=${page}&size=${size}`);
  }
}
