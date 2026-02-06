import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },

  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/pages/dashboard-page/dashboard.page')
        .then(m => m.DashboardPage),
  },
  {
    path: 'hospedes/novo',
    loadComponent: () =>
      import('./features/hospedes/pages/hospede-create/hospede-create.page')
        .then(m => m.HospedeCreatePage),
  },
  {
    path: 'checkins/novo',
    loadComponent: () =>
      import('./features/checkins/pages/checkin-create/checkin-create.page')
        .then(m => m.CheckInCreatePage),
  },
  {
    path: 'consultas',
    loadComponent: () =>
      import('./features/consultas/pages/consultas/consultas.page')
        .then(m => m.ConsultasPage),
  },

  { path: '**', redirectTo: 'hospedes/novo' }
];
