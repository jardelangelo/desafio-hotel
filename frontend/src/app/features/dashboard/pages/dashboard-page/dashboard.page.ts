import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HospedeCreateModalComponent } from '../../../hospedes/components/hospede-create-modal/hospede-create-modal.component';
import { CheckInPanelComponent } from '../../../checkins/components/checkin-panel/checkin-panel.component';
import { ConsultasPanelComponent } from '../../../consultas/components/consultas-panel/consultas-panel.component';

@Component({
  standalone: true,
  selector: 'app-dashboard-page',
  imports: [CommonModule, HospedeCreateModalComponent, CheckInPanelComponent, ConsultasPanelComponent],
  templateUrl: './dashboard.page.html',
  styleUrls: ['./dashboard.page.scss']
})
export class DashboardPage {
  modalOpen = false;

  openModal() { this.modalOpen = true; }
  closeModal() { this.modalOpen = false; }
}
