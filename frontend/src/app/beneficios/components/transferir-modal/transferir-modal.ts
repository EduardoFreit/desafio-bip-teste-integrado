import { Component, EventEmitter, inject, Output, signal, TemplateRef, ViewChild, WritableSignal } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { BeneficioDTO, GerenciamentoDeBenefciosService, ListarRequestParams, PageBeneficioDTO, TransferenciaRequest, TransferirRequestParams } from '../../../api';
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

  tranferirObjeto : TransferenciaRequest = { contaOrigemId: 0, contaDestinoId: 0, valor: 0.00 };

  beneficiosDestino?: Array<BeneficioDTO> = [];

	open(tranferir: TransferirRequestParams) {
    this.tranferirObjeto = { ...tranferir.transferenciaRequest };
    this.beneficiosDestino = this.beneficiosDestino?.filter(b => b.id !== this.tranferirObjeto.contaOrigemId);
		this.modalService.open(this.contentTemplate, { ariaLabelledBy: 'modal-basic-title' });
	}

  constructor(
    private beneficioApi: GerenciamentoDeBenefciosService
  ) {
    this.listarBeneficiosDestino();
  }

  listarBeneficiosDestino() {
    let filtroBeneficio : ListarRequestParams = {
        page: 0,
        size: 10000,
        sort: ['nome,asc']
      };
      this.beneficioApi.listar(filtroBeneficio).subscribe((resp: PageBeneficioDTO) => {
        this.beneficiosDestino = resp.content;
      });
  }

  transferir() {
    let transferirRequest: TransferirRequestParams = { transferenciaRequest: this.tranferirObjeto };
    this.transferido.emit(transferirRequest);
  }

}
