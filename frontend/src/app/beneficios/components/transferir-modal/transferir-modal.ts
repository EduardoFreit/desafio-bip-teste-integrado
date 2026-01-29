import { Component, EventEmitter, inject, Output, signal, TemplateRef, ViewChild, WritableSignal } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { BeneficioDTO, GerenciamentoDeBenefciosService, TransferenciaRequest, TransferirRequestParams } from '../../../api';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-transferir-modal',
  imports: [FormsModule, CommonModule],
  templateUrl: './transferir-modal.html',
  styleUrl: './transferir-modal.css',
})
export class TransferirModal {

  private modalService = inject(NgbModal);
  closeResult: WritableSignal<string> = signal('');
  @ViewChild('content') contentTemplate!: TemplateRef<any>;

  @Output() transferido = new EventEmitter<TransferirRequestParams>();

  transferenciaRequest: TransferenciaRequest = { contaOrigemId: 0, contaDestinoId: 0, valor: 0.00 };

  beneficiosDestino?: Array<BeneficioDTO> = [];

  constructor(
    private beneficioApi: GerenciamentoDeBenefciosService
  ) {
  }

  open(transferir: TransferirRequestParams) {
    this.beneficioApi.listarTodos().subscribe({
      next: (beneficios: Array<BeneficioDTO>) => {
        console.log(beneficios);
        this.beneficiosDestino = Array.isArray(beneficios) ? beneficios : [];
        this.transferenciaRequest = { ...transferir.transferenciaRequest };
        this.beneficiosDestino = (this.beneficiosDestino || []).filter(b => b.id !== this.transferenciaRequest.contaOrigemId);
        this.modalService.open(this.contentTemplate, { ariaLabelledBy: 'modal-basic-title' });
      },
      error: (error) => {
        console.error('Erro ao listar benefícios para transferência:', error);
      }
    });

  }

  transferir() {
    this.modalService.dismissAll();
    let transferirRequest: TransferirRequestParams = { transferenciaRequest: this.transferenciaRequest };
    this.transferido.emit(transferirRequest);
  }

}
