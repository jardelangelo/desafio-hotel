export interface GastoHospede {
  idHospede: number;
  nome: string;
  documento: string;
  telefone: string;

  dataEntrada: string;
  dataSaida: string;
  adicionalVeiculo: boolean;

  valorTotalGasto: number;
  valorUltimaHospedagem: number;
}
