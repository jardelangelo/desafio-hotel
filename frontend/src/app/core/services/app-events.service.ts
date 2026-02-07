import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Guest } from '../models/guest.model';

@Injectable({ providedIn: 'root' })
export class AppEventsService {
  private readonly refreshConsultasSubject = new Subject<void>();
  readonly refreshConsultas$ = this.refreshConsultasSubject.asObservable();

  private readonly guestCreatedSubject = new Subject<Guest>();
  readonly guestCreated$ = this.guestCreatedSubject.asObservable();

  refreshConsultas() {
    this.refreshConsultasSubject.next();
  }

  guestCreated(guest: Guest) {
    this.guestCreatedSubject.next(guest);
  }
}
